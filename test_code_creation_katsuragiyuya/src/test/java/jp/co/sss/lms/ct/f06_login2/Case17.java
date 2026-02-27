package jp.co.sss.lms.ct.f06_login2;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
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
 * ケース17
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース17 受講生 初回ログイン 正常系")
public class Case17 {

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
	@DisplayName("テスト04 変更パスワードを入力し「変更」ボタン押下")
	void test04() {

		pageLoadTimeout(5);
		visibilityTimeout(By.id("currentPassword"), 10);

		String nowPassword = "StudentAA03";
		String correctPassword = "StudentAA03a";

		//パスワードを入力
		webDriver.findElement(By.id("currentPassword")).sendKeys(nowPassword);
		webDriver.findElement(By.id("password")).sendKeys(correctPassword);
		webDriver.findElement(By.id("passwordConfirm")).sendKeys(correctPassword);

		//JSを実行し、ページの最下部までスクロール
		scrollBy("document.body.scrollHeight");

		//入力したパスワードのエビデンスを取得
		getEvidence(new Object() {
		}, "passwordChange");

		//変更ボタンを押下
		webDriver.findElement(By.xpath("//button[text()='変更']")).click();

		pageLoadTimeout(5);
		//モーダルウィンドウの要素が出現するまで待機
		visibilityTimeout(By.id("upd-btn"), 5);
		//モーダルウィンドウの更新ボタンの要素を取得
		WebElement updateButton = webDriver.findElement(By.id("upd-btn"));
		//モーダルウィンドウの更新ボタンを押下
		updateButton.click();
		// 画面右上の「ようこそ」メッセージ要素を取得
		WebElement welcomMsg = webDriver.findElement(By.xpath("//a[@href='/lms/user/detail']/small"));
		// 取得したテキストが期待通り表示されているかを確認
		assertThat(welcomMsg.getText(), containsString("ようこそ受講生ＡＡ３さん"));

		//正しくパスワードできた場合詳細画面に遷移するため遷移後のエビデンスを取得
		getEvidence(new Object() {
		}, "passwordChangeSuccessful");
	}

}
