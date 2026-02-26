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
 * ケース15
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース15 受講生 初回ログイン 利用規約に不同意")
public class Case15 {

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
		login("StudentAA02", "StudentAA02");
		// 取得したテキストが期待通り表示されているかを確認
		assertEquals("セキュリティ規約 | LMS", webDriver.getTitle());

		getEvidence(new Object() {
		}, "firstlogin");

	}

	@Test
	@Order(3)
	@DisplayName("テスト03 「同意します」チェックボックスにチェックをせず「次へ」ボタンを押下")
	void test03() {
		//JSを実行し、ページの最下部までスクロール
		scrollBy("document.body.scrollHeight");
		//次へを押下
		webDriver.findElement(By.cssSelector("button.btn-primary")).click();
		pageLoadTimeout(5);
		//JSを実行し、ページの最下部までスクロール
		scrollBy("document.body.scrollHeight");
		//エラーメッセージを取得
		WebElement errorMsg = webDriver.findElement(By.className("error"));
		//内容の比較
		assertEquals("セキュリティ規約への同意は必須です。", errorMsg.getText());

		getEvidence(new Object() {
		}, "SecurityError");

	}

}
