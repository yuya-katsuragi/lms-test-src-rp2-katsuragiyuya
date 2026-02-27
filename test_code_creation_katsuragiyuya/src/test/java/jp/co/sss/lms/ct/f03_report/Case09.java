package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * 結合テスト レポート機能
 * ケース09
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
public class Case09 {

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
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		//loginメソッドを呼び出しid,password,ログインボタンを取得
		login("StudentAA01", "StudentAA01a");
		// 画面右上の「ようこそ」メッセージ要素を取得
		WebElement welcomMsg = webDriver.findElement(By.xpath("//a[@href='/lms/user/detail']/small"));
		// 取得したテキストが期待通り表示されているかを確認
		assertEquals("ようこそ受講生ＡＡ１さん", welcomMsg.getText());

		getEvidence(new Object() {
		}, "loginSuccessful");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() {
		// リンクテキストにようこそという文字列が含まれる要素を探してクリックする
		webDriver.findElement(By.partialLinkText("ようこそ")).click();

		//指定したタイトル文字列になるまで待機
		titleTimeout("ユーザー詳細", 5);
		//内容の比較
		assertEquals("ユーザー詳細", webDriver.getTitle());

		getEvidence(new Object() {
		}, "welcomeText");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		scrollBy("document.body.scrollHeight");
		// 「提出する」ボタンをクリック
		webDriver.findElement(By.xpath("//td[contains(text(), '週報【デモ】')]/following-sibling::td//input[@value='修正する']"))
				.click();
		//指定したタイトル文字列になるまで待機
		titleTimeout("レポート登録 | LMS", 5);
		//内容の比較
		assertEquals("レポート登録 | LMS", webDriver.getTitle());

		getEvidence(new Object() {
		}, "correctionScreen");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習項目が未入力")
	void test05() {
		Select select = new Select(webDriver.findElement(By.id("intFieldValue_0")));

		//value属性で選択
		select.selectByValue("1");

		webDriver.findElement(By.id("intFieldName_0")).clear();
		//JSを実行し、ページの最下部までスクロール
		scrollBy("document.body.scrollHeight");
		// 「提出する」ボタンをクリック
		webDriver.findElement(By.xpath("//button[text()='提出する']")).click();

		//エラー文が出現するまで待機
		visibilityTimeout(By.cssSelector(".help-inline.error"), 5);
		//エラー文取得
		WebElement errorMsg = webDriver.findElement(By.cssSelector(
				".help-inline.error"));

		//内容を比較
		assertEquals("* 理解度を入力した場合は、学習項目は必須です。", errorMsg.getText());

		getEvidence(new Object() {
		}, "learningItemsNotEntered");

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：理解度が未入力")
	void test06() {

		//Selectオブジェクトを生成しintFieldValue_0を指定
		Select select = new Select(webDriver.findElement(By.id("intFieldValue_0")));

		//value属性で選択
		select.selectByValue("");
		// 学習項目名に文字を入力
		webDriver.findElement(By.id("intFieldName_0")).sendKeys("waaaaaai");

		//JSを実行し、ページの最下部までスクロール
		scrollBy("document.body.scrollHeight");

		//「提出する」ボタンをクリック
		webDriver.findElement(By.xpath("//button[text()='提出する']")).click();

		//エラー文が出現するまで待機
		visibilityTimeout(By.cssSelector(".help-inline.error"), 5);
		//エラー文取得
		WebElement errorMsg = webDriver.findElement(By.cssSelector(
				".help-inline.error"));
		//内容を比較
		assertEquals("* 学習項目を入力した場合は、理解度は必須です。", errorMsg.getText());

		getEvidence(new Object() {
		}, "UnderstandingNotEntered");
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が数値以外")
	void test07() {
		//Selectオブジェクトを生成しintFieldValue_0を指定
		Select select = new Select(webDriver.findElement(By.id("intFieldValue_0")));

		//value属性で選択
		select.selectByValue("2");
		// 目標の達成度をクリア
		webDriver.findElement(By.id("content_0")).clear();
		// 目標の達成度数値以外の文字を入力
		webDriver.findElement(By.id("content_0")).sendKeys("今日はハナマル");

		scrollBy("document.body.scrollHeight");
		// 「提出する」ボタンをクリック
		webDriver.findElement(By.xpath("//button[text()='提出する']")).click();

		//300px分下にスクロール
		scrollBy("300");

		//エラー文が出現するまで待機
		visibilityTimeout(By.cssSelector(".help-inline.error"), 5);
		//エラー文取得
		WebElement errorMsg = webDriver.findElement(By.cssSelector(
				".help-inline.error"));
		//内容を比較
		assertEquals("* 目標の達成度は半角数字で入力してください。", errorMsg.getText());

		getEvidence(new Object() {
		}, "nonNumericValue");

	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が範囲外")
	void test08() {
		// 目標の達成度をクリア
		webDriver.findElement(By.id("content_0")).clear();
		// 目標の達成度に範囲外の数値を入力
		webDriver.findElement(By.id("content_0")).sendKeys("100");
		// JSを実行し、ページの最下部までスクロール
		scrollBy("document.body.scrollHeight");
		// 「提出する」ボタンをクリック
		webDriver.findElement(By.xpath("//button[text()='提出する']")).click();
		//300px分下にスクロール
		scrollBy("300");

		//エラー文が出現するまで待機
		visibilityTimeout(By.cssSelector(".help-inline.error"), 5);
		//エラー文取得
		WebElement errorMsg = webDriver.findElement(By.cssSelector(
				".help-inline.error"));
		//内容を比較
		assertEquals("* 目標の達成度は、半角数字で、1～10の範囲内で入力してください。", errorMsg.getText());

		getEvidence(new Object() {
		}, "otherThanNumericalValues");
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() {
		// 目標の達成度をクリア
		webDriver.findElement(By.id("content_0")).clear();
		// 所感をクリア
		webDriver.findElement(By.id("content_1")).clear();
		// JSを実行し、ページの最下部までスクロール
		scrollBy("document.body.scrollHeight");
		// 「提出する」ボタンをクリック
		webDriver.findElement(By.xpath("//button[text()='提出する']")).click();
		//300px分下にスクロール
		scrollBy("300");
		//エラー文が出現するまで待機
		visibilityTimeout(By.cssSelector(".help-inline.error"), 5);

		//エラー文を複数取得
		List<WebElement> errorMsgs = webDriver.findElements(By.cssSelector(".help-inline.error"));

		//内容を比較（目標の達成度）
		assertEquals("* 目標の達成度は半角数字で入力してください。", errorMsgs.get(0).getText());
		//内容を比較（所感）
		assertEquals("* 所感は必須です。", errorMsgs.get(1).getText());

		getEvidence(new Object() {
		}, "achievementAssessmentNotEntered");
	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() {
		// 2000文字の文字列を生成
		String overtext = "あ".repeat(2000);
		// 目標の達成度10を入力
		webDriver.findElement(By.id("content_0")).sendKeys("10");
		//所感フィールドに入力
		webDriver.findElement(By.id("content_1")).sendKeys("週報のサンプルです。");
		//一週間の振り返りフィールドに2000文字を入力
		webDriver.findElement(By.id("content_2")).sendKeys(overtext);
		// JSを実行し、ページの最下部までスクロール
		scrollBy("document.body.scrollHeight");
		// 「提出する」ボタンをクリック
		webDriver.findElement(By.xpath("//button[text()='提出する']")).click();
		// JSを実行し、ページの最下部までスクロール
		scrollBy("document.body.scrollHeight");

		//エラー文が出現するまで待機
		visibilityTimeout(By.cssSelector(".help-inline.error"), 5);
		//エラー文取得
		WebElement errorMsg = webDriver.findElement(By.cssSelector(
				".help-inline.error"));
		//内容を比較
		assertEquals("* 一週間の振り返りの長さが最大値(2000)を超えています。", errorMsg.getText());

		getEvidence(new Object() {
		}, "over2000Characters");
	}

}
