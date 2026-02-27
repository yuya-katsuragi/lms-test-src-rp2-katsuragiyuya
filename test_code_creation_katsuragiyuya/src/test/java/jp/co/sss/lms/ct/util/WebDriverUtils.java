package jp.co.sss.lms.ct.util;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.io.Files;

/**
 * Webドライバーユーティリティ
 * @author holy
 */
public class WebDriverUtils {

	/** Webドライバ */
	public static WebDriver webDriver;

	/**
	 * インスタンス取得
	 * @return Webドライバ
	 */
	private static WebDriverUtils instance;

	//要素の定義（フィールドとして定義
	@FindBy(id = "loginId")
	private WebElement loginIdFiled;

	@FindBy(id = "password")
	private WebElement passWordFiled;

	@FindBy(css = "input.btn-primary[value='ログイン']")
	private WebElement loginButton;

	public static void createDriver() {
		System.setProperty("webdriver.chrome.driver", "lib/chromedriver.exe");
		webDriver = new ChromeDriver();
		instance = new WebDriverUtils();

	}

	/**
	 * インスタンス終了
	 */
	public static void closeDriver() {
		webDriver.quit();
	}

	/**
	 * 画面遷移
	 * @param url
	 */
	public static void goTo(String url) {
		webDriver.get(url);
		pageLoadTimeout(5);
	}

	/**
	 * ページロードタイムアウト設定
	 * @param second
	 */
	public static void pageLoadTimeout(int second) {
		webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(second));
	}

	/**
	 * 要素の可視性タイムアウト設定
	 * @param locater
	 * @param second
	 */
	public static void visibilityTimeout(By locater, int second) {
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(second));
		wait.until(ExpectedConditions.visibilityOfElementLocated(locater));
	}

	/**
	 * 指定ピクセル分だけスクロール
	 * @param pixel
	 */
	public static void scrollBy(String pixel) {
		((JavascriptExecutor) webDriver).executeScript("window.scrollBy(0," + pixel + ");");
	}

	/**
	 * 指定位置までスクロール
	 * @param pixel
	 */
	public static void scrollTo(String pixel) {
		((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0," + pixel + ");");
	}

	/**
	 * エビデンス取得
	 * @param instance
	 */
	public static void getEvidence(Object instance) {
		File tempFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		try {
			String className = instance.getClass().getEnclosingClass().getSimpleName();
			String methodName = instance.getClass().getEnclosingMethod().getName();
			Files.move(tempFile, new File("evidence\\" + className + "_" + methodName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * エビデンス取得（サフィックスあり）
	 * @param instance
	 * @param suffix
	 */
	public static void getEvidence(Object instance, String suffix) {
		File tempFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		try {
			String className = instance.getClass().getEnclosingClass().getSimpleName();
			String methodName = instance.getClass().getEnclosingMethod().getName();
			Files.move(tempFile, new File("evidence\\" + className + "_" + methodName + "_" + suffix + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * タイトルタイムアウト設定
	 * @param title
	 * @param second
	 */
	public static void titleTimeout(String title, int second) {
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(second));
		wait.until(ExpectedConditions.titleIs(title));
	}

	//コンストラクタ（初期化)
	public WebDriverUtils() {
		PageFactory.initElements(webDriver, this);
	}

	//アクションメソッドを定義
	public static void login(String login, String password) {
		instance.loginIdFiled.sendKeys(login);
		instance.passWordFiled.sendKeys(password);
		instance.loginButton.click();
	}
}
