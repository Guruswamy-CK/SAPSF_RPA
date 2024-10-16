package com.rpa.sapsf.service.impl;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.web.multipart.MultipartFile;

import com.rpa.sapsf.service.FileGeneratorService;

import io.github.bonigarcia.wdm.WebDriverManager;

public class FileGeneratorServiceImpl implements FileGeneratorService {

	@Override
	public String generateWbpWorkbook(MultipartFile file) {
		WebDriverManager.chromedriver().clearDriverCache().setup();
		ChromeOptions options = new ChromeOptions();
		// options.addArguments("--headless");
		options.addArguments("start-maximized");
		WebDriver driver = new ChromeDriver(options);
		WebDriverWait waitDriver = new WebDriverWait(driver, Duration.ofSeconds(30)); // 10-second explicit wait
		driver.get("https://salesdemo.successfactors.eu/login?company=SFPART058183");
		WebElement username = waitDriver.until(ExpectedConditions.visibilityOfElementLocated(By.id("__input1-inner")));
		WebElement password = waitDriver.until(ExpectedConditions.visibilityOfElementLocated(By.id("__input2-inner")));
		WebElement login = waitDriver.until(ExpectedConditions.visibilityOfElementLocated(By.id("__button2-inner")));
		username.sendKeys("admin1");
		password.sendKeys("Felicia123");
		login.click();
		WebElement shadowHost1 = waitDriver
				.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("xweb-shellbar")));
		SearchContext shadowRoot1 = shadowHost1.getShadowRoot();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebElement shadowHost2 = shadowRoot1.findElement(By.cssSelector("#search"));
		SearchContext shadowRoot2 = shadowHost2.getShadowRoot();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebElement searchButton = shadowRoot2.findElement(By.cssSelector("#ui5wc_29-inner"));
		searchButton.sendKeys("Admin Center");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		searchButton.sendKeys(Keys.ENTER);
		WebElement seeAllLink = waitDriver.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[@class='sapMLnk sapMLnkMaxWidth link' and text()='See All']")));
		seeAllLink.click();
		WebElement managePermissionRolesLink = waitDriver.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[@class='sapMLnk adminLinkAnchor globalTileLink' and text()='Manage Permission Roles']")));
		managePermissionRolesLink.click();

		WebElement tableRoles = waitDriver
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='tableBody_21:']")));
		List<WebElement> rolesList = tableRoles.findElements(By.xpath("//tbody[@id='tbody_21:']/tr/td[2]/div/a"));
		for (WebElement weRole : rolesList) {
			weRole.click();
			break;
		}

		WebElement perminssionLink = waitDriver.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"//*[@class='globalRoundedCornersXSmall globalSecondaryButton buttonDefault fd-button fd-button--compact' and text()='Permission...']")));
		perminssionLink.click();

		WebElement permissionItemParent = waitDriver.until(
				ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul[class='permission_items_list']")));
		List<WebElement> permissionItemsList = permissionItemParent.findElements(By.xpath("//li/a"));
		int index = 0;
		for (WebElement weItem : permissionItemsList) {
			index = index + 1;
			if (index > 3)
				break;
			System.out.println();
			System.out.println();
			System.out.println("================");
			System.out.println(weItem.getText().trim());
			System.out.println("================");
			weItem.click();
			List<WebElement> templatePermissionsList = permissionItemParent
					.findElements(By.xpath("//li/div/select/option"));
			for (WebElement weTPerm : templatePermissionsList) {
				System.out.println(weTPerm.getText().trim());
			}
		}
		return null;
	}

	@Override
	public String generateJobWorkbook(MultipartFile file) {
		WebDriverManager.chromedriver().clearDriverCache().setup();
		ChromeOptions options = new ChromeOptions();
		// options.addArguments("--headless");
		options.addArguments("start-maximized");
		WebDriver driver = new ChromeDriver(options);
		WebDriverWait waitDriver = new WebDriverWait(driver, Duration.ofSeconds(60)); // 10-second explicit wait
		driver.get("https://salesdemo.successfactors.eu/login?company=SFPART058183");
		WebElement username = waitDriver.until(ExpectedConditions.visibilityOfElementLocated(By.id("__input1-inner")));
		WebElement password = waitDriver.until(ExpectedConditions.visibilityOfElementLocated(By.id("__input2-inner")));
		WebElement login = waitDriver.until(ExpectedConditions.visibilityOfElementLocated(By.id("__button2-inner")));
		username.sendKeys("admin1");
		password.sendKeys("Felicia123");
		login.click();
		WebElement shadowHost1 = waitDriver
				.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("xweb-shellbar")));
		SearchContext shadowRoot1 = shadowHost1.getShadowRoot();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebElement shadowHost2 = shadowRoot1.findElement(By.cssSelector("#shellbar"));
		SearchContext shadowRoot2 = shadowHost2.getShadowRoot();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebElement menuButton = shadowRoot2.findElement(
				By.cssSelector("button[class=' ui5-shellbar-menu-button--interactive ui5-shellbar-menu-button ']"));
		menuButton.click();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		WebElement shadowMenuHost1 = driver.findElement(By.xpath(
				"//ui5-static-area//ui5-static-area-item-sf-header[@class='ui5wc_1 sapUiSizeCompact ui5-content-density-compact globalShellbarMenu sfEnsureSAPTheme']"));

		/*
		 * WebElement perminssionLink =
		 * waitDriver.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
		 * "//ui5-static-area//ui5-static-area-item-sf-header[@class='ui5wc_1 sapUiSizeCompact ui5-content-density-compact globalShellbarMenu sfEnsureSAPTheme']"
		 * )));
		 * 
		 * WebElement shadowMenuHost1 =
		 * waitDriver.until(ExpectedConditions.visibilityOfElementLocated(By.
		 * cssSelector(
		 * "ui5-static-area-item-sf-header[class='ui5wc_1 sapUiSizeCompact ui5-content-density-compact globalShellbarMenu sfEnsureSAPTheme']"
		 * )));
		 */

		SearchContext shadowMenuRoot1 = shadowMenuHost1.getShadowRoot();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		WebElement shadowMenuHost2 = shadowMenuRoot1
				.findElement(By.cssSelector("ui5-popover-sf-header[class='ui5-shellbar-menu-popover']"));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebElement orgChartParent = shadowMenuHost2.findElement(By.tagName("ui5-list-sf-header"));
		WebElement orgChart = orgChartParent.findElement(By.cssSelector("ui5-li-sf-header[icon='org-chart']"));
		WebElement orgChartEvent = orgChart.findElement(By.tagName("a"));
		orgChartEvent.click();

		WebElement shadowHost11 = waitDriver
				.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("xweb-shellbar")));
		SearchContext shadowRoot11 = shadowHost11.getShadowRoot();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebElement shadowHost12 = shadowRoot11.findElement(By.cssSelector("#subnav"));
		SearchContext shadowRoot12 = shadowHost12.getShadowRoot();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebElement positionOrgChatButton = shadowRoot12
				.findElement(By.xpath("//a[@class='subTabLink' and text()='Position Org Chart']"));

		positionOrgChatButton.click();

		return null;
	}

}
