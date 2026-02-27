package jp.co.sss.lms.ct.f06_login2;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 結合テスト ログイン機能②
 * ケース16
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース16 受講生 初回ログイン 変更パスワード未入力")
public class Case16 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		goTo("http://localhost:8080/lms/");

		assertEquals("ログイン | LMS", webDriver.getTitle());

		getEvidence(new Object() {
		}, "topPageAccess");
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 DBに初期登録された未ログインの受講生ユーザーでログイン")
	void test02() {
		//loginメソッドを呼び出しid,password,ログインボタンを取得
		login("StudentAA03", "StudentAA03");
		// 取得したテキストが期待通り表示されているかを確認
		assertEquals("セキュリティ規約 | LMS", webDriver.getTitle());

		getEvidence(new Object() {
		}, "firstlogin");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 「同意します」チェックボックスにチェックを入れ「次へ」ボタン押下")
	void test03() {
		pageLoadTimeout(5);
		//JSを実行し、ページの最下部までスクロール
		scrollBy("document.body.scrollHeight");
		//チェックボックスの要素を取得
		WebElement checkBox = webDriver.findElement(By.name("securityFlg"));
		//チェックボックスを押下
		checkBox.click();
		//内容を比較
		assertEquals("セキュリティ規約 | LMS", webDriver.getTitle());
		getEvidence(new Object() {
		}, "SecuritySuccess");

		//getEvidenceより後に押下することで正しいエビデンスを取得
		webDriver.findElement(By.cssSelector("button.btn-primary")).click();

		getEvidence(new Object() {
		}, "passwordChangeScreen");

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 パスワードを未入力で「変更」ボタン押下")
	void test04() {

		//JSを実行し、ページの最下部までスクロール
		scrollBy("document.body.scrollHeight");

		//変更ボタンを押下
		webDriver.findElement(By.xpath("//button[text()='変更']")).click();

		//モーダルウィンドウの要素が出現するまで待機
		visibilityTimeout(By.id("upd-btn"), 5);
		//モーダルウィンドウの更新ボタンの要素を取得
		WebElement updateButton = webDriver.findElement(By.id("upd-btn"));
		//モーダルウィンドウの更新ボタンを押下
		updateButton.click();

		//現在のパスワード欄のエラーメッセージを取得
		String currentPassError = webDriver
				.findElement(By.xpath("//label[@for='currentPassword']/..//span[@class='help-inline error']"))
				.getText();
		//新しいパスワード欄のエラーメッセージを取得(複数のエラー文)
		String newPassError = webDriver
				.findElement(By.xpath("//label[@for='password']/..//span[@class='help-inline error']")).getText();
		//確認パスワード欄のエラーメッセージを取得
		String confirmPassError = webDriver
				.findElement(By.xpath("//label[@for='passwordConfirm']/..//span[@class='help-inline error']"))
				.getText();

		//JSを実行し、ページの最下部までスクロール
		scrollBy("document.body.scrollHeight");

		//現在のパスワード欄のエラーメッセージを比較
		assertEquals("現在のパスワードは必須です。", currentPassError);
		// 複数の文章が含まれるため、containsで部分一致の確認をしています
		assertTrue(newPassError.contains("パスワードは必須です。"));
		//確認パスワード欄のエラーメッセージを比較
		assertEquals("確認パスワードは必須です。", confirmPassError);

		getEvidence(new Object() {
		}, "notEnteredPassword");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 20文字以上の変更パスワードを入力し「変更」ボタン押下")
	void test05() {
		String nowPassword = "StudentAA03";
		//20文字以上のパスワードを設定
		String overPassword = "Qwerasdfzxcv123456789";

		pageLoadTimeout(5);

		//パスワードを入力
		webDriver.findElement(By.id("currentPassword")).sendKeys(nowPassword);
		webDriver.findElement(By.id("password")).sendKeys(overPassword);
		webDriver.findElement(By.id("passwordConfirm")).sendKeys(overPassword);

		//JSを実行し、ページの最下部までスクロール
		scrollBy("document.body.scrollHeight");

		//変更ボタンを押下
		webDriver.findElement(By.xpath("//button[text()='変更']")).click();

		pageLoadTimeout(5);
		//モーダルウィンドウの要素が出現するまで待機
		visibilityTimeout(By.id("upd-btn"), 5);
		//モーダルウィンドウの更新ボタンの要素を取得
		WebElement updateButton = webDriver.findElement(By.id("upd-btn"));
		//モーダルウィンドウの更新ボタンを押下
		updateButton.click();

		//エラーメッセージを取得
		WebElement errorMsg = webDriver.findElement(
				By.xpath("//span[contains(@class,'error') and contains(text(),'パスワードの長さが最大値(20)を超えています。')]"));

		//内容を比較
		assertEquals("パスワードの長さが最大値(20)を超えています。", errorMsg.getText());

		//JSを実行し、ページの最下部までスクロール
		scrollBy("document.body.scrollHeight");
		getEvidence(new Object() {
		}, "overPassword");

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 ポリシーに合わない変更パスワードを入力し「変更」ボタン押下")
	void test06() {
		String nowPassword = "StudentAA03";
		// 大文字が含まれていない
		String nonPolicyPassword = "a12345678";

		pageLoadTimeout(5);
		//パスワードを入力
		webDriver.findElement(By.id("currentPassword")).sendKeys(nowPassword);
		webDriver.findElement(By.id("password")).sendKeys(nonPolicyPassword);
		webDriver.findElement(By.id("passwordConfirm")).sendKeys(nonPolicyPassword);
		//JSを実行し、ページの最下部までスクロール
		scrollBy("document.body.scrollHeight");

		//変更ボタンを押下
		webDriver.findElement(By.xpath("//button[text()='変更']")).click();

		pageLoadTimeout(5);
		//モーダルウィンドウの要素が出現するまで待機
		visibilityTimeout(By.id("upd-btn"), 5);
		//モーダルウィンドウの更新ボタンの要素を取得
		WebElement updateButton = webDriver.findElement(By.id("upd-btn"));
		//モーダルウィンドウの更新ボタンを押下
		updateButton.click();

		//エラーメッセージを取得
		WebElement errorMsg = webDriver.findElement(
				By.xpath("//span[contains(@class,'error') and contains(text(),"
						+ "'「パスワード」には半角英数字のみ使用可能です。"
						+ "また、半角英大文字、半角英小文字、数字を含めた8～20文字を入力してください。')]"));
		//内容を比較
		assertEquals("「パスワード」には半角英数字のみ使用可能です。"
				+ "また、半角英大文字、半角英小文字、数字を含めた"
				+ "8～20文字を入力してください。", errorMsg.getText());
		//JSを実行し、ページの最下部までスクロール
		scrollBy("document.body.scrollHeight");
		getEvidence(new Object() {
		}, "doNotPolicyPassword");

	}

	@Test
	@Order(7)
	@DisplayName("テスト07 一致しない確認パスワードを入力し「変更」ボタン押下")
	void test07() {

		//パスワードを入力（確認パスワードは意図的に異なる値を設定）
		webDriver.findElement(By.id("currentPassword")).sendKeys("StudentAA03");
		webDriver.findElement(By.id("password")).sendKeys("StudentAA03a");
		webDriver.findElement(By.id("passwordConfirm")).sendKeys("StudentAA033");

		//JSを実行し、ページの最下部までスクロール
		scrollBy("document.body.scrollHeight");

		//変更ボタンを押下
		webDriver.findElement(By.xpath("//button[text()='変更']")).click();

		pageLoadTimeout(5);
		//モーダルウィンドウの要素が出現するまで待機
		visibilityTimeout(By.id("upd-btn"), 5);
		//モーダルウィンドウの更新ボタンの要素を取得
		WebElement updateButton = webDriver.findElement(By.id("upd-btn"));
		//モーダルウィンドウの更新ボタンを押下
		updateButton.click();

		//エラーメッセージを取得
		WebElement errorMsg = webDriver.findElement(
				By.xpath("//span[contains(@class,'error') and contains(text(),'パスワードと確認パスワードが一致しません。')]"));

		//内容を比較
		assertEquals("パスワードと確認パスワードが一致しません。", errorMsg.getText());

		//JSを実行し、ページの最下部までスクロール
		scrollBy("document.body.scrollHeight");
		getEvidence(new Object() {
		}, "unmatchedPassword");
	}

}
