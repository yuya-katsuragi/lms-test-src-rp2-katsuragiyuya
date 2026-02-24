package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
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

/**
 * 結合テスト よくある質問機能
 * ケース05
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース05 キーワード検索 正常系")
public class Case05 {

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
		}, "login Successful");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		//ドロップダウンメニューの親要素をクリックして、展開
		webDriver.findElement(By.linkText("機能")).click();
		//展開されたメニュー内のヘルプリンクをクリック
		webDriver.findElement(By.xpath("//a[@href='/lms/help']")).click();
		//遷移先のタイトルが期待通りで表示されているか確認
		assertEquals("ヘルプ | LMS", webDriver.getTitle());

		getEvidence(new Object() {
		}, "helpScreen");

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		// 「よくある質問」のリンク要素を特定
		WebElement commonQuestions = webDriver.findElement(By.xpath("//a[@href='/lms/faq']"));
		// リンクをクリック（target="_blank" により新しいタブが開く）
		commonQuestions.click();
		// ウィンドウハンドルを取得
		ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
		// 新しいタブに切り替え
		webDriver.switchTo().window(tabs.get(1));
		assertEquals("よくある質問 | LMS", webDriver.getTitle());

		getEvidence(new Object() {
		}, "commonQuestions");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 キーワード検索で該当キーワードを含む検索結果だけ表示")
	void test05() {

		webDriver.findElement(By.id("form")).sendKeys("研修");
		webDriver.findElement(By.xpath("//input[@value='検索']")).click();

		List<WebElement> resultList = webDriver.findElements(By.className("mb10"));

		assertEquals(2, resultList.size());

		getEvidence(new Object() {
		}, "KeywordSearch");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 「クリア」ボタン押下で入力したキーワードを消去")
	void test06() {

		webDriver.findElement(By.xpath("//input[@value='クリア']")).click();

		assertEquals("", webDriver.findElement(By.id("form")).getAttribute("value"));

		getEvidence(new Object() {
		}, "ClearKeyword");
	}

}
