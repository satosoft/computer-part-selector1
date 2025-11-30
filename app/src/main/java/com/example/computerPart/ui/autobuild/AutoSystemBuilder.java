package com.example.computerPart.ui.autobuild;

import com.example.computerPart.dao.*;
import com.example.computerPart.model.*;
import com.example.computerPart.ui.autobuild.AutoBuildResultsPanel.SystemCombination;
import com.example.computerPart.ui.wizard.WizardComponentManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.swing.Timer;
import java.awt.event.ActionListener;

public class AutoSystemBuilder extends JDialog {
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final AutoBuildStartPanel step1Panel;
    private final AutoBuildResultsPanel resultsPanel;
    private final JButton backButton;
    private final JButton nextButton;
    private int currentStep = 1;
    private final java.util.concurrent.atomic.AtomicBoolean timeoutReached = new java.util.concurrent.atomic.AtomicBoolean(
            false);
    private final AtomicInteger timeRemaining = new AtomicInteger();

    public AutoSystemBuilder(JFrame parent) {
        super(parent, "Auto System Builder", true);
        // setSize(1000, 700);
        // setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // componentManager = WizardComponentManager.getInstance();
        setResizable(true);

        // Get the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Set the dialog size to 90% of screen size
        setSize((int) (screenSize.width * 0.9), (int) (screenSize.height * 0.9));
        setLocationRelativeTo(parent);

        // Create the card layout and panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create the panels
        step1Panel = new AutoBuildStartPanel();
        resultsPanel = new AutoBuildResultsPanel();

        // Add panels to the card layout
        cardPanel.add(step1Panel, "step1");
        cardPanel.add(resultsPanel, "results");

        // Create the navigation buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backButton = new JButton("Back");
        nextButton = new JButton("Next");

        backButton.addActionListener(e -> handleBack());
        nextButton.addActionListener(e -> handleNext());

        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);

        // Add components to the dialog
        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Show the first panel
        cardLayout.show(cardPanel, "step1");
        updateButtonStates();
    }

    private void handleBack() {
        if (currentStep > 1) {
            currentStep--;
            resultsPanel.clearCombinations();
            cardLayout.show(cardPanel, currentStep == 1 ? "step1" : "results");
            updateButtonStates();
        }
    }

    private void handleNext() {
        if (currentStep == 1) {
            if (step1Panel.validateInput()) {
                // Check if auto-select is requested
                /*
                 * if (step1Panel.isAutoSelectRequested()) {
                 * step1Panel.resetAutoSelectRequest(); // Reset the flag
                 * performAutoSelection();
                 * return;
                 * }
                 */

                // Normal flow
                currentStep++;
                cardLayout.show(cardPanel, "results");
                updateButtonStates();
                performAutoSelection();
            }
        } else if (currentStep == 2) {
            // Handle export functionality
            exportToExcel();
        }
    }

    private void performAutoSelection() {
        try {
            // Get the max search time
            int maxSearchTimeSeconds = step1Panel.getMaxSearchTime();

            // Show a progress dialog
            JDialog progressDialog = new JDialog(this, "Auto-selecting components...", true);
            progressDialog.setLayout(new BorderLayout());

            // Create a panel for the progress bar and time remaining
            JPanel progressPanel = new JPanel(new BorderLayout());
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);

            // Create a label for the time remaining
            JLabel timeLabel = new JLabel("Time remaining: " + maxSearchTimeSeconds + " seconds");
            timeLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));

            progressPanel.add(progressBar, BorderLayout.CENTER);
            progressPanel.add(timeLabel, BorderLayout.SOUTH);

            JLabel statusLabel = new JLabel("Processing...");
            statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            progressDialog.add(progressPanel, BorderLayout.CENTER);
            progressDialog.add(statusLabel, BorderLayout.SOUTH);
            progressDialog.setSize(350, 150);
            progressDialog.setLocationRelativeTo(this);

            // Create a timer to update the time remaining
            Timer countdownTimer = new Timer(1000, null);
            // int timeRemaining = maxSearchTimeSeconds;
            // AtomicInteger timeRemaining = new AtomicInteger(maxSearchTimeSeconds);
            timeRemaining.set(maxSearchTimeSeconds);
            // Create a worker thread to perform the auto-selection
            SwingWorker<List<SystemCombination>, String> worker = new SwingWorker<>() {
                @Override
                protected List<SystemCombination> doInBackground() throws Exception {
                    try {
                        publish("Loading components...");
                        return autoSelectComponents();
                    } catch (Exception e) {
                        e.printStackTrace();
                        publish("Error: " + e.getMessage());
                        return new ArrayList<>();
                    }
                }

                @Override
                protected void process(List<String> chunks) {
                    // Update the status label with the latest message
                    if (!chunks.isEmpty()) {
                        statusLabel.setText(chunks.get(chunks.size() - 1));
                    }
                }

                @Override
                protected void done() {
                    // Stop the countdown timer
                    // countdownTimer.stop();
                    if (isCancelled()) {
                        progressDialog.dispose();
                        return;
                    }

                    try {
                        List<SystemCombination> combinations = get();
                        progressDialog.dispose();

                        if (combinations.isEmpty()) {
                            JOptionPane.showMessageDialog(AutoSystemBuilder.this,
                                    "No compatible combinations found within your budget.\n" +
                                            "Please try again with different requirements or a higher budget.",
                                    "No Results",
                                    JOptionPane.WARNING_MESSAGE);
                        } else {
                            // Add the combinations to the results panel
                            resultsPanel.clearCombinations();
                            for (SystemCombination combo : combinations) {
                                resultsPanel.addCombination(
                                        combo.mainboard, combo.cpu, combo.ram, combo.gpu,
                                        combo.hardDisk, combo.cooler, combo.computerCase,
                                        combo.powerSource, combo.cpuQuantity, combo.ramQuantity,
                                        combo.gpuQuantity);
                            }

                            // Skip to the results panel
                            currentStep = 2;
                            cardLayout.show(cardPanel, "results");
                            updateButtonStates();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(AutoSystemBuilder.this,
                                "Error during auto-selection: " + e.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            };

            countdownTimer.addActionListener(e -> {
                // timeRemaining--;
                int currentTime = timeRemaining.decrementAndGet();

                if (currentTime < 0) {
                    timeLabel.setText("Time remaining: 0 seconds");
                    // countdownTimer.stop();
                    worker.cancel(true);
                } else {
                    timeLabel.setText("Time remaining: " + timeRemaining + " seconds");
                }
            });
            // Start the worker thread
            worker.execute();

            // Start the countdown timer
            countdownTimer.start();

            // Show the progress dialog
            progressDialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error during auto-selection: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<SystemCombination> autoSelectComponents() throws Exception {
        // Get user preferences
        double userBudget = step1Panel.getBudget();
        String preferredColor = step1Panel.getPreferredColor();
        int preferredCpuQty = step1Panel.getCpuNumber() > 0 ? step1Panel.getCpuNumber() : 1;
        int preferredGpuQty = step1Panel.getGpuNumber() > 0 ? step1Panel.getGpuNumber() : 1;
        int preferredMinGpuCapacity = step1Panel.getGpuMinCapacity();
        int preferredMaxGpuCapacity = step1Panel.getGpuMaxCapacity();
        int preferredRamCapacity = step1Panel.getRamCapacity();
        String preferredMainSize = step1Panel.getMainSize();
        int maxSearchTimeSeconds = step1Panel.getMaxSearchTime();
        String preferredHdType = step1Panel.getHdType();
        int preferredHdMinCapacity = step1Panel.getHdMinCapacity();
        int preferredHdmaxCapacity = step1Panel.getHdMaxCapacity();
        System.out.println("Auto-selecting with budget: " + userBudget +
                ", color: " + preferredColor +
                ", CPU qty: " + preferredCpuQty +
                ", GPU qty: " + preferredGpuQty +
                ", RAM capacity: " + preferredRamCapacity +
                ", mainboard size: " + preferredMainSize +
                ", max search time: " + maxSearchTimeSeconds + " seconds");

        // Load all components
        MainboardDAO mainboardDAO = new MainboardDAO();
        CPUDAO cpuDAO = new CPUDAO();
        RAMDAO ramDAO = new RAMDAO();
        GPUDAO gpuDAO = new GPUDAO();
        HardDiskDAO hardDiskDAO = new HardDiskDAO();
        CoolerDAO coolerDAO = new CoolerDAO();
        ComputerCaseDAO computerCaseDAO = new ComputerCaseDAO();
        PowerSourceDAO powerSourceDAO = new PowerSourceDAO();

        // Get all suitable mainboards and sort by price
        List<Mainboard> allMainboards = mainboardDAO.getAll();
        List<Mainboard> suitableMainboards = allMainboards.stream()
                .filter(mb -> mb.getMainSize().equals(preferredMainSize))
                .filter(mb -> "Any".equals(preferredColor)
                        || mb.getColor().equals(preferredColor))
                .filter(mb -> mb.getPrice() <= (userBudget))
                .filter(mb -> mb.getCpuSlots() >= preferredCpuQty)
                .filter(mb -> mb.getGpuSlots() >= preferredGpuQty)
                .filter(mb -> mb.getStock() > 0)
                .collect(Collectors.toList());
        if (preferredHdType.equals("NVME")) {
            suitableMainboards = suitableMainboards.stream()
                    .filter(mb -> mb.isSupportNVME())
                    .collect(Collectors.toList());
        }
        // System.out.println("Total mainboards loaded: " + mainboards.size());
        suitableMainboards.sort(Comparator.comparing(Mainboard::getPrice));

        // Create a thread pool
        // int numThreads = Math.min(Runtime.getRuntime().availableProcessors(),
        // suitableMainboards.size());
        // ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        ExecutorService executor = Executors.newCachedThreadPool();

        // Create a synchronized list to store the top combinations
        List<SystemCombination> topCombinations = Collections.synchronizedList(new ArrayList<>());

        // Create a countdown latch to wait for all threads to complete
        // CountDownLatch latch = new CountDownLatch(suitableMainboards.size());

        // Create an atomic counter to track progress
        java.util.concurrent.atomic.AtomicInteger processedMainboards = new java.util.concurrent.atomic.AtomicInteger(
                0);
        java.util.concurrent.atomic.AtomicInteger compatibleMainboards = new java.util.concurrent.atomic.AtomicInteger(
                0);

        java.util.concurrent.atomic.AtomicInteger foundCombinations = new java.util.concurrent.atomic.AtomicInteger(0);

        // Create a flag to indicate if the search should stop due to timeout

        // Process each mainboard in a separate thread
        for (Mainboard mainboard : suitableMainboards) {
            if (timeRemaining.get() <= 0)
                break;
            processedMainboards.incrementAndGet();

            executor.submit(() -> {
                try {
                    // Check if timeout has been reached before processing
                    if (timeRemaining.get() <= 0) {
                        System.out.println("Skipping mainboard due to timeout: " + mainboard.getName());
                        return;
                    }

                    processMainboard(
                            mainboard, cpuDAO, ramDAO, gpuDAO, hardDiskDAO, coolerDAO,
                            computerCaseDAO, powerSourceDAO, topCombinations,
                            userBudget, preferredColor, preferredCpuQty, preferredGpuQty, preferredMinGpuCapacity,
                            preferredMaxGpuCapacity,
                            preferredRamCapacity, preferredMainSize, preferredHdType, preferredHdMinCapacity,
                            preferredHdmaxCapacity,
                            processedMainboards, compatibleMainboards, foundCombinations);
                } catch (Exception e) {
                    e.printStackTrace();
                } // finally {
                  // latch.countDown();
                  // }
            });
        }
        executor.shutdown();
        executor.awaitTermination(maxSearchTimeSeconds, TimeUnit.SECONDS);
        // Start a timer to enforce the maximum search time
        ScheduledExecutorService timeoutExecutor = Executors.newSingleThreadScheduledExecutor();
        timeoutExecutor.schedule(() -> {
            System.out.println("Max search time reached (" + maxSearchTimeSeconds + " seconds).Stopping search...");
            timeoutReached.set(true);
            executor.shutdownNow();
        }, maxSearchTimeSeconds, TimeUnit.SECONDS);

        // try {
        // Wait for all threads to complete or until the timeout is reached
        // boolean completed = latch.await(maxSearchTimeSeconds, TimeUnit.SECONDS);
        // if (!completed) {
        // System.out.println("Search timed out after " + maxSearchTimeSeconds +
        // "seconds");
        // }
        // } finally {
        // Shutdown the executors
        // timeoutExecutor.shutdownNow();
        // executor.shutdownNow();
        // }
        // executor.shutdown();
        // executor.awaitTermination(maxSearchTimeSeconds, TimeUnit.SECONDS);
        // timeoutExecutor.shutdownNow();
        System.out.println("Auto-selection complete. Processed " + processedMainboards.get() + " mainboards, found "
                + compatibleMainboards.get() + " compatible mainboards and " + foundCombinations.get()
                + " total combinations.");

        if (timeRemaining.get() <= 0) {
            System.out.println("Search was stopped due to reaching the maximum search time of " +
                    maxSearchTimeSeconds + " seconds");
        }

        // Sort the combinations by benchmark score (descending) and take the top 10
        topCombinations.sort((c1, c2) -> Double.compare(c2.getTotalBenchmark(), c1.getTotalBenchmark()));

        // Return the top 10 combinations (or fewer if less than 10 are found)
        return topCombinations.stream().limit(100).collect(Collectors.toList());
    }

    private void processMainboard(
            Mainboard mainboard, CPUDAO cpuDAO, RAMDAO ramDAO, GPUDAO gpuDAO,
            HardDiskDAO hardDiskDAO, CoolerDAO coolerDAO, ComputerCaseDAO computerCaseDAO,
            PowerSourceDAO powerSourceDAO, List<SystemCombination> topCombinations,
            double userBudget, String preferredColor, int preferredCpuQty, int preferredGpuQty,
            int preferredMinGpuCapacity, int preferredMaxGpuCapacity,
            int preferredRamCapacity, String preferredMainSize, String preferredHdType, int preferredHdMinCapacity,
            int preferredHdMaxCapacity,
            java.util.concurrent.atomic.AtomicInteger processedMainboards,
            java.util.concurrent.atomic.AtomicInteger compatibleMainboards,
            java.util.concurrent.atomic.AtomicInteger foundCombinations) throws SQLException {

        if (timeRemaining.get() <= 0) {
            return;
        }

        // // Step 2: Check mainboard compatibility
        // if (mainboard.getPrice() > userBudget) {
        // return; // Skip if mainboard exceeds budget
        // }

        // if (!"Any".equals(preferredColor) &&
        // !preferredColor.equals(mainboard.getColor())) {
        // return; // Skip if color doesn't match
        // }

        // if (!"Any".equals(preferredMainSize) &&
        // !preferredMainSize.equals(mainboard.getMainSize())) {
        // return; // Skip if mainboard size doesn't match
        // }

        // if (mainboard.getCpuSlots() < preferredCpuQty) {
        // return; // Skip if not enough CPU slots//
        // }

        // if (mainboard.getGpuSlots() < preferredGpuQty) {
        // return; // Skip if not enough GPU slots//
        // }
        // if (!mainboard.isSupportNVME() && preferredHdType.equals("NVME")) {
        // return; // Skip if not enough NVMe slots//
        // }
        // if (mainboard.getStock() <= 0) {
        // return;
        // }

        compatibleMainboards.incrementAndGet();
        System.out.println("Suitable mainboard found: " + mainboard.getName() +
                " (Socket: " + mainboard.getCpuSocket() +
                ", RAM Type: " + mainboard.getRamType() +
                ", Size: " + mainboard.getMainSize() + ")");

        double selectecMbPrice = mainboard.getPrice();
        double cpuRemainingBudget = userBudget - selectecMbPrice;

        // Step 3: Choose CPU
        List<CPU> cpus = cpuDAO.getAll();
        System.out.println("Total CPUs: " + cpus.size() + " for socket: " + mainboard.getCpuSocket());

        List<CPU> compatibleCPUs = cpus.stream()
                .filter(cpu -> cpu.getSocket().equals(mainboard.getCpuSocket()))
                .filter(cpu -> cpu.getPrice() * preferredCpuQty <= cpuRemainingBudget)
                .filter(cpu -> cpu.getStock() >= preferredCpuQty)
                .collect(Collectors.toList());

        System.out.println("Compatible CPUs: " + compatibleCPUs.size() + " for mainboard: " + mainboard.getName());

        if (compatibleCPUs.isEmpty()) {
            return; // No compatible CPUs found
        }

        for (CPU cpu : compatibleCPUs) {
            if (timeRemaining.get() <= 0) {
                return;
            }

            double cpuTotalPrice = cpu.getPrice() * preferredCpuQty;
            double ramRemainingBudget = cpuRemainingBudget - cpuTotalPrice;

            // Step 4: Choose RAM
            List<RAM> rams = ramDAO.getAll();
            System.out.println("Total RAMs: " + rams.size() + " for type: " + mainboard.getRamType());

            List<RAM> compatibleRAMs = rams.stream()
                    // .filter(ram -> ram.getRamType().equals(mainboard.getRamType()))
                    .filter(ram -> "Any".equals(preferredColor) || preferredColor.equals(ram.getColor()))
                    .filter(ram -> {
                        // Calculate how many modules we need
                        int bRamCapacity = preferredRamCapacity;
                        int sRamCapacity = ram.getCapacity();
                        int availableSlots = mainboard.getRamSlots();
                        int availableStock = ram.getStock();
                        int neededModules = (int) Math.ceil((double) (bRamCapacity) / sRamCapacity);
                        String sRamType = ram.getRamType();
                        String rRamType = mainboard.getRamType();
                        double remBudget = ramRemainingBudget;
                        double requiredBudget = ram.getPrice() * neededModules;
                        boolean suitableWithMainboard = (availableSlots >= neededModules);
                        boolean suitableRamType = (sRamType.equals(rRamType));
                        boolean suitableCapacity = ((bRamCapacity % sRamCapacity) == 0);
                        boolean suitableStock = (availableStock >= neededModules);
                        boolean budgetMatch = (remBudget >= requiredBudget);
                        return budgetMatch && suitableWithMainboard && suitableRamType && suitableCapacity
                                && suitableStock;
                    })
                    .collect(Collectors.toList());

            System.out.println("Compatible RAMs: " + compatibleRAMs.size() + " for mainboard: " + mainboard.getName());

            if (compatibleRAMs.isEmpty()) {
                if (timeRemaining.get() <= 0) {
                    return;
                }
                continue; // No compatible RAMs found, try next CPU
            }

            for (RAM ram : compatibleRAMs) {
                if (timeRemaining.get() <= 0) {
                    return;
                }

                // Calculate how many RAM modules are needed
                int ramQty = (int) Math.ceil((double) preferredRamCapacity / ram.getCapacity());

                // Check if mainboard has enough RAM slots
                // if (mainboard.getRamSlots() < ramQty) {
                // continue; // Not enough RAM slots, try next RAM
                // }

                // Check if RAM is in stock
                // if (ram.getStock() < ramQty) {
                // continue; // Not enough RAM in stock, try next RAM
                // }

                double ramTotalPrice = ram.getPrice() * ramQty;

                // Check if RAM fits in budget
                // if (ramTotalPrice > cpuRemainingBudget) {
                // continue; // RAM exceeds budget, try next RAM
                // }

                double gpuRemainingBudget = ramRemainingBudget - ramTotalPrice;

                // Step 5: Choose GPU
                List<GPU> gpus = gpuDAO.getAll();
                System.out.println("Total GPUs: " + gpus.size());

                List<GPU> compatibleGPUs = gpus.stream()
                        .filter(gpu -> "Any".equals(preferredColor) || preferredColor.equals(gpu.getColor()))
                        .filter(gpu -> gpu.getStock() >= preferredGpuQty)
                        .filter(gpu -> {
                            int requiredGpuNumber = preferredGpuQty;
                            int availableGpuNumber = gpu.getStock();
                            // int availableGpuSlots = componentManager.selectedMainboard.getGpuSlots();
                            double remBudget = gpuRemainingBudget;
                            double requiredBudget = gpu.getPrice() * requiredGpuNumber;
                            // boolean mainboardMatch = (availableGpuSlots >= requiredGpuNumber);
                            boolean stockMatch = (availableGpuNumber >= requiredGpuNumber);
                            boolean budgetMatch = (requiredBudget <= remBudget);
                            return budgetMatch && stockMatch;
                        })
                        .filter(gpu -> {
                            int sGpuCapacity = gpu.getCapacity();
                            int rMinGpuCapacity = preferredMinGpuCapacity;
                            int rMaxGpuCapacity = preferredMaxGpuCapacity;
                            boolean gpuCapacityMatch = (rMinGpuCapacity <= sGpuCapacity
                                    && rMaxGpuCapacity >= sGpuCapacity);
                            return gpuCapacityMatch;
                        })
                        .collect(Collectors.toList());

                System.out.println("Compatible GPUs: " + compatibleGPUs.size());

                if (compatibleGPUs.isEmpty()) {
                    if (timeRemaining.get() <= 0) {
                        return;
                    }
                    continue; // No compatible GPUs found, try next RAM
                }

                for (GPU gpu : compatibleGPUs) {
                    if (timeRemaining.get() <= 0) {
                        return;
                    }
                    double gpuTotalPrice = gpu.getPrice() * preferredGpuQty;

                    // Check if GPU fits in budget
                    // if (gpuTotalPrice > ramRemainingBudget) {
                    // continue; // GPU exceeds budget, try next GPU
                    // }

                    double hdRemainingBudget = gpuRemainingBudget - gpuTotalPrice;

                    // Step 6: Choose Hard Disk
                    List<HardDisk> hardDisks = hardDiskDAO.getAll();
                    System.out.println("Total Hard Disks: " + hardDisks.size());

                    List<HardDisk> compatibleHardDisks = hardDisks.stream()
                            .filter(hd -> hd.getPrice() <= hdRemainingBudget)
                            .filter(hd -> hd.getStock() > 0)
                            .filter(hd -> {
                                int minCapacity = preferredHdMinCapacity;
                                int maxCapacity = preferredHdMaxCapacity;
                                int sCapacity = hd.getCapacity();
                                boolean capacityMatch = (sCapacity >= minCapacity && sCapacity <= maxCapacity);
                                return capacityMatch;
                            })
                            .filter(hd -> {
                                String sType = hd.getType();
                                String bType = preferredHdType;
                                boolean typeMatch = (bType.equals("Any") || bType.equals(sType));
                                return typeMatch;
                            })
                            .collect(Collectors.toList());

                    System.out.println("Compatible Hard Disks: " + compatibleHardDisks.size());

                    if (compatibleHardDisks.isEmpty()) {
                        if (timeRemaining.get() <= 0) {
                            return;
                        }
                        continue; // No compatible hard disks found, try next GPU
                    }

                    for (HardDisk hardDisk : compatibleHardDisks) {
                        if (timeRemaining.get() <= 0) {
                            return;
                        }
                        double coolerRemainingBudget = hdRemainingBudget - hardDisk.getPrice();

                        // Step 7: Choose Cooler - Select the cheapest compatible cooler
                        List<Cooler> coolers = coolerDAO.getAll();
                        System.out.println(
                                "Total Coolers: " + coolers.size() + " for socket: " + mainboard.getCpuSocket());

                        List<Cooler> compatibleCoolers = coolers.stream()
                                .filter(cooler -> {
                                    // Check if cooler supports the CPU socket
                                    for (String socket : cooler.getSocketSupport()) {
                                        if (socket.trim().equals(mainboard.getCpuSocket())) {
                                            return true;
                                        }
                                    }
                                    return false;
                                })
                                .filter(cooler -> "Any".equals(preferredColor)
                                        || preferredColor.equals(cooler.getColor()))
                                .filter(cooler -> ((cooler.getPrice() * preferredCpuQty) <= coolerRemainingBudget))
                                .filter(cooler -> cooler.getStock() >= preferredCpuQty)
                                .collect(Collectors.toList());

                        System.out.println("Compatible Coolers: " + compatibleCoolers.size() + " for socket: "
                                + mainboard.getCpuSocket());

                        if (compatibleCoolers.isEmpty()) {
                            if (timeRemaining.get() <= 0) {
                                return;
                            }
                            continue; // No compatible coolers found, try next hard disk
                        }

                        // Select the cheapest compatible cooler
                        Cooler cheapestCooler = compatibleCoolers.stream()
                                .min(Comparator.comparing(Cooler::getPrice))
                                .orElse(null);

                        if (cheapestCooler == null) {
                            if (timeRemaining.get() <= 0) {
                                return;
                            }
                            continue; // This shouldn't happen, but just in case
                        }

                        double caseRemainingBudget = coolerRemainingBudget
                                - cheapestCooler.getPrice() * preferredCpuQty;

                        // Step 8: Choose Computer Case - Select the cheapest compatible case
                        List<ComputerCase> cases = computerCaseDAO.getAll();
                        System.out.println("Total Cases: " + cases.size() + " for size: " + mainboard.getMainSize());

                        List<ComputerCase> compatibleCases = cases.stream()
                                .filter(cc -> cc.getCaseSize().equals(mainboard.getMainSize()))
                                .filter(cc -> "Any".equals(preferredColor) || preferredColor.equals(cc.getColor()))
                                .filter(cc -> cc.getPrice() <= caseRemainingBudget)
                                .filter(cc -> cc.getStock() > 0)
                                .collect(Collectors.toList());

                        System.out.println("Compatible Cases: " + compatibleCases.size() + " for size: "
                                + mainboard.getMainSize());

                        if (compatibleCases.isEmpty()) {
                            if (timeRemaining.get() <= 0) {
                                return;
                            }
                            continue; // No compatible cases found, try next cooler
                        }

                        // Select the cheapest compatible case
                        ComputerCase cheapestCase = compatibleCases.stream()
                                .min(Comparator.comparing(ComputerCase::getPrice))
                                .orElse(null);

                        if (cheapestCase == null) {
                            if (timeRemaining.get() <= 0) {
                                return;
                            }
                            continue; // This shouldn't happen, but just in case
                        }

                        // double caseRemainingBudget = coolerRemainingBudget - cheapestCase.getPrice();
                        double psRemainingBudget = caseRemainingBudget - cheapestCase.getPrice();
                        // Calculate total power
                        int totalPower = mainboard.getPower() +
                                (cpu.getPower() * preferredCpuQty) +
                                (ram.getPower() * ramQty) +
                                (gpu.getPower() * preferredGpuQty) +
                                hardDisk.getPower() +
                                cheapestCooler.getPower() +
                                cheapestCase.getPower();

                        // Add 20% safety margin
                        // totalPower = (int) (totalPower * 1.2);

                        // Create a final copy for use in lambda
                        // final int requiredPower = totalPower;

                        // Step 9: Choose Power Source - Select the cheapest compatible power source
                        List<PowerSource> powerSources = powerSourceDAO.getAll();
                        // System.out.println("Total Power Sources: " + powerSources.size()
                        // + " for required power: " + totalPower + "W");

                        List<PowerSource> compatiblePowerSources = powerSources.stream()
                                .filter(ps -> ps.getPower() >= totalPower)
                                .filter(ps -> "Any".equals(preferredColor)
                                        || preferredColor.equals(ps.getColor()))
                                .filter(ps -> ps.getPrice() <= psRemainingBudget)
                                .filter(ps -> ps.getStock() > 0)
                                .collect(Collectors.toList());

                        System.out.println("Compatible Power Sources: " + compatiblePowerSources.size()
                                + " for required power: " + totalPower + "W");

                        if (compatiblePowerSources.isEmpty()) {
                            if (timeRemaining.get() <= 0) {
                                return;
                            }
                            continue; // No compatible power sources found, try next case
                        }

                        // Select the cheapest compatible power source
                        PowerSource cheapestPowerSource = compatiblePowerSources.stream()
                                .min(Comparator.comparing(PowerSource::getPrice))
                                .orElse(null);

                        if (cheapestPowerSource == null) {
                            if (timeRemaining.get() <= 0) {
                                return;
                            }
                            continue; // This shouldn't happen, but just in case
                        }

                        // Calculate total price
                        double totalSystemPrice = mainboard.getPrice() +
                                (cpu.getPrice() * preferredCpuQty) +
                                (ram.getPrice() * ramQty) +
                                (gpu.getPrice() * preferredGpuQty) +
                                hardDisk.getPrice() +
                                cheapestCooler.getPrice() +
                                cheapestCase.getPrice() +
                                cheapestPowerSource.getPrice();

                        // Check if total price is within budget
                        if (totalSystemPrice <= userBudget) {
                            // Create a new system combination
                            SystemCombination combination = new SystemCombination(
                                    mainboard, cpu, ram, gpu, hardDisk, cheapestCooler,
                                    cheapestCase, cheapestPowerSource, preferredCpuQty,
                                    ramQty, preferredGpuQty);

                            // Add to the list of top combinations
                            synchronized (topCombinations) {
                                topCombinations.add(combination);
                                int count = foundCombinations.incrementAndGet();
                                // if (timeoutReached.get())
                                // return;
                                System.out.println("Found combination #" + count + " with total price: VND" +
                                        totalSystemPrice + " and benchmark: "
                                        + combination.getTotalBenchmark() + "Time remaining: " + timeRemaining.get());
                                if (timeRemaining.get() <= 0) {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateButtonStates() {
        backButton.setEnabled(currentStep > 1);
        if (currentStep == 2) {
            nextButton.setText("Export");
        } else {
            nextButton.setText("Start Auto Build");
        }
    }

    private void exportToExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Excel File");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            try {
                resultsPanel.exportToExcel(filePath);
                JOptionPane.showMessageDialog(this,
                        "Export successful!\nFile saved to: " + filePath,
                        "Export Complete",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error exporting to Excel: " + e.getMessage(),
                        "Export Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void showDialog(JFrame parent) {
        AutoSystemBuilder dialog = new AutoSystemBuilder(parent);
        dialog.setVisible(true);
    }
}
