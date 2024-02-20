//Implement a Multithreaded Asynchronous Image Downloader in Java Swing
//Task Description:
//You are tasked with designing and implementing a multithreaded asynchronous image downloader in a Java Swing
//application. The application should allow users to enter a URL and download images from that URL in the
//background, while keeping the UI responsive. The image downloader should utilize multithreading and provide a
//smooth user experience when downloading images.
//Requirements:
//Design and implement a GUI application that allows users to enter a URL and download images.
//Implement a multithreaded asynchronous framework to handle the image downloading process in the background.
//Provide a user interface that displays the progress of each image download, including the current download status
//and completion percentage.
//Utilize a thread pool to manage the concurrent downloading of multiple images, ensuring efficient use of system
//resources.
//Implement a mechanism to handle downloading errors or exceptions, displaying appropriate error messages to the
//user.
//Use thread synchronization mechanisms, such as locks or semaphores, to ensure data integrity and avoid conflicts
//during image downloading.
//Provide options for the user to pause, resume, or cancel image downloads.
//Test the application with various URLs containing multiple images to verify its functionality and responsiveness.
//Include proper error handling and reporting for cases such as invalid URLs or network failures

package src.Question6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageDownloader extends JFrame {
    private JTextField urlTextField;
    private JButton downloadButton;
    private JButton pauseButton;
    private JButton cancelButton;
    private JProgressBar progressBar;
    private JPanel imagePanel;

    private ExecutorService executorService;
    private boolean isCanceled;
    private boolean isPaused;
    private final Object pauseLock = new Object();


    public ImageDownloader() {
        setTitle("Image Downloader");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initComponents();
        setupLayout();
        setupListeners();

        executorService = Executors.newFixedThreadPool(5);
        isCanceled = false;
    }

    private void initComponents() {
        urlTextField = new JTextField(40);
        downloadButton = new JButton("Download");
        pauseButton = new JButton("Pause");
        cancelButton = new JButton("Cancel");
        progressBar = new JProgressBar();
        imagePanel = new JPanel();
        imagePanel.setLayout(new GridLayout(0, 4, 10, 10));

        Font buttonFont = new Font(Font.SANS_SERIF, Font.BOLD, 14);
        downloadButton.setFont(buttonFont);
        pauseButton.setFont(buttonFont);
        cancelButton.setFont(buttonFont);
        progressBar.setStringPainted(true); // Show progress string
        progressBar.setFont(buttonFont);
        urlTextField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel inputPanel = createInputPanel();
        JPanel progressPanel = createProgressPanel();
        JPanel centerPanel = createCenterPanel();

        mainPanel.add(inputPanel);
        mainPanel.add(progressPanel);
        mainPanel.add(centerPanel);

        // Add some padding
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(mainPanel);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel urlLabel = new JLabel("URL: ");
        urlLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        inputPanel.add(urlLabel);
        inputPanel.add(urlTextField);
        inputPanel.add(downloadButton);
        return inputPanel;
    }

    private JPanel createProgressPanel() {
        JPanel progressPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        progressPanel.add(progressBar);
        progressPanel.add(pauseButton);
        progressPanel.add(cancelButton);
        return progressPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        return centerPanel;
    }

    private void setupListeners() {
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] urls = urlTextField.getText().split("\\s+");
                for (String url : urls) {
                    downloadImage(url.trim());
                }
                urlTextField.setText(""); // Clear text field after download
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pause or resume all downloads
                if (pauseButton.getText().equals("Pause")) {
                    pauseButton.setText("Resume");
                    isPaused = true;
                } else {
                    pauseButton.setText("Pause");
                    isPaused = false;
                    synchronized (pauseLock) {
                        pauseLock.notify();
                    }
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isCanceled = true;
                downloadButton.setEnabled(true);
            }
        });
    }


    private void downloadImage(String urlString) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                int totalBytesRead = 0;
                int fileSize = 0;
                InputStream in = null;
                ByteArrayOutputStream out = null;

                try {
                    progressBar.setValue(0);

                    URL url = new URL(urlString);
                    URLConnection connection = url.openConnection();
                    fileSize = connection.getContentLength();
                    in = new BufferedInputStream(connection.getInputStream());
                    out = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    int n;

                    while (-1 != (n = in.read(buf))) {
                        synchronized (pauseLock) {
                            while (isPaused) {
                                pauseLock.wait();
                            }
                        }

                        if (isCanceled) {
                            return;
                        }

                        out.write(buf, 0, n);
                        totalBytesRead += n;
                        int progress = (int) ((double) totalBytesRead / fileSize * 100);
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                progressBar.setValue(progress);
                            }
                        });

                        Thread.sleep(200);
                    }

                    final byte[] response = out.toByteArray();
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            ImageIcon imageIcon = new ImageIcon(response);
                            JLabel imageLabel = new JLabel(imageIcon);
                            imagePanel.add(imageLabel);
                            JOptionPane.showMessageDialog(null, "Image downloaded successfully from URL: " + urlString);
                            progressBar.setValue(0);
                            revalidate();
                            repaint();
                        }
                    });
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            JOptionPane.showMessageDialog(null, "Failed to download image from URL: " + urlString + "\nError: " + e.getMessage());
                        }
                    });
                } finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ImageDownloader imageDownloader = new ImageDownloader();
                imageDownloader.setLocationRelativeTo(null);
                imageDownloader.setVisible(true);
            }
        });
    }
}
