package com.fpt.ida.idasignhub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class IdaSignHubApplication {


	public static void main(String[] args) throws IOException {
		final Path rootLocation = Paths.get("config");
		// Chạy SystemTray khi ứng dụng Spring Boot khởi động
		if (!SystemTray.isSupported()) {
			System.out.println("System tray not supported!");
			return;
		}
		SystemTray systemTray = SystemTray.getSystemTray();
		String pathIcon = rootLocation.toAbsolutePath().toString() + "/IDA.png";
		Image image = new ImageIcon(pathIcon).getImage();
		PopupMenu trayPopupMenu = new PopupMenu();
		MenuItem exitItem = new MenuItem("Thoát chương trình");
		exitItem.addActionListener(e -> {
			System.out.println("Exiting...");
			System.exit(0);
		});
		// Tạo menu item "Open Link"
		MenuItem goToDashboard = new MenuItem("Trình điều khiển trung tâm");
		goToDashboard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					System.out.println("go to dashboard ...");
					// Mở URL trong trình duyệt mặc định
					Desktop desktop = Desktop.getDesktop();
					URI url = new URI("http://localhost:8434/");
					desktop.browse(url);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		TrayIcon trayIcon = new TrayIcon(image, "IDA SignHub Tool", trayPopupMenu);
		trayIcon.setImageAutoSize(true);
		trayPopupMenu.add(goToDashboard);
		trayPopupMenu.add(exitItem);
		try {
			systemTray.add(trayIcon);
			System.out.println("Application started in system tray.");
		} catch (AWTException e) {
			e.printStackTrace();
		}
//		trayIcon.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseReleased(MouseEvent e) {
//				// If the user right-clicks, show the custom JPopupMenu
//				if (e.isMetaDown()) {
//					System.out.println("Popup triggered!");
////					showCustomPopupMenu(e.getX(), e.getY());
//				}
//			}
//		});
		SpringApplication.run(IdaSignHubApplication.class, args);
	}

}
