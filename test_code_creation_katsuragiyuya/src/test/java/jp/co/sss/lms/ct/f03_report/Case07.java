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
 * ケース07
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース07 受講生 レポート新規登録(日報) 正常系")
public class Case07 {

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
	@DisplayName("テスト03 未提出の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {

		//300px分、下にスクロール
		scrollBy("300");
		// セクションIDが3のフォーム内にある送信ボタンを特定してクリック
		webDriver.findElement(By.xpath("//form[.//input[@name='sectionId' and "
				+ "@value='4']]//input[@type='submit']")).click();

		//指定したタイトル文字列になるまで待機
		titleTimeout("セクション詳細 | LMS", 5);

		//内容の比較
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());

		getEvidence(new Object() {
		}, "notSubmitted");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「提出する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		//日報【デモ】を提出するを押下
		webDriver.findElement(By.cssSelector("input[value='日報【デモ】を提出する']")).click();
		//指定したタイトル文字列になるまで待機
		titleTimeout("レポート登録 | LMS", 5);
		//内容の比較
		assertEquals("レポート登録 | LMS", webDriver.getTitle());

		getEvidence(new Object() {
		}, "sectionScreen");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を入力して「提出する」ボタンを押下し確認ボタン名が更新される")
	void test05() {
		//指定したテキストに入力
		webDriver.findElement(By.id("content_0")).sendKeys("明日も頑張ります！");
		//提出ボタンを押下
		webDriver.findElement(By.xpath("//button[text()='提出する']")).click();

		getEvidence(new Object() {
		}, "reportRegistration ");
	}

}
