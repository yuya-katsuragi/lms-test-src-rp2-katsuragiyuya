package jp.co.sss.lms.ct.f03_report;

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
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

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
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		// セクションIDが2のフォーム内にある送信ボタンを特定してクリック
		webDriver.findElement(By.xpath("//form[.//input[@name='sectionId' and @value='2']]//input[@type='submit']"))
				.click();

		//指定したタイトル文字列になるまで待機
		titleTimeout("セクション詳細 | LMS", 5);
		//内容の比較
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());

		getEvidence(new Object() {
		}, "detailSubmit");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		//JSを実行し、ページの最下部までスクロール
		scrollBy("document.body.scrollHeight");
		//提出済み日報【デモ】を確認するを押下
		webDriver.findElement(By.cssSelector("input[value='提出済み日報【デモ】を確認する']")).click();

		//指定したタイトル文字列になるまで待機
		titleTimeout("レポート登録 | LMS", 5);
		//内容の比較
		assertEquals("レポート登録 | LMS", webDriver.getTitle());

		getEvidence(new Object() {
		}, "confirmationSubmit");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() {
		//報告内容に記載されている記述をクリア
		webDriver.findElement(By.id("content_0")).clear();
		//報告内容を記述
		webDriver.findElement(By.id("content_0")).sendKeys("今日はいい感じに進みました");
		//提出するボタンをクリック
		webDriver.findElement(By.xpath("//button[text()='提出する']")).click();

		//指定したタイトル文字列になるまで待機
		titleTimeout("セクション詳細 | LMS", 5);
		//内容の比較
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());

		getEvidence(new Object() {
		}, "submissionSubmit");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() {
		// リンクテキストにようこそという文字列が含まれる要素を探してクリックする
		webDriver.findElement(By.partialLinkText("ようこそ")).click();

		//指定したタイトル文字列になるまで待機
		titleTimeout("ユーザー詳細", 5);
		//指定したタイトル文字列になるまで待機
		assertEquals("ユーザー詳細", webDriver.getTitle());

		getEvidence(new Object() {
		}, "welcomeText");
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() {
		//JSを実行し、ページの最下部までスクロール
		scrollBy("document.body.scrollHeight");
		// IDが2のフォーム内にある送信ボタンを特定してクリック
		webDriver.findElement(
				By.xpath("//form[.//input[@name='dailyReportSubmitId' and @value='2']]//input[@type='submit']"))
				.click();
		//(th)の隣にあるセル(td)から文字列を取得し、前後の空白を除去
		String actual = webDriver.findElement(
				By.xpath("//th[text()='本日の報告内容をお書きください。']/following-sibling::td")).getText().trim();

		//内容の比較
		assertEquals("今日はいい感じに進みました", actual);

		getEvidence(new Object() {
		}, "reflectCorrections");
	}

}
