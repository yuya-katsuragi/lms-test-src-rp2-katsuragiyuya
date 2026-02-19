package jp.co.sss.lms.ct.f01_login1;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 結合テスト ログイン機能①
 * ケース01
 * @author holy
 */
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース01 ログイン画面への遷移")
public class Case01 {

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

	/**
	 * Case.1_1 ログインコントローラー試験　初期表示テスト(未ログイン時 正常終了)
	 * ■対象メソッド：index() 
	 * ■試験パラメータ：なし
	 * ■試験観点：
	 *  ・正常終了すること
	 *  ・HTTPステータスが「200」であること
	 *  ・返却されるViewのパスが「/login/index」であること
	 *  
	 * @throws Exception 
	 * */
	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		goTo("http://localhost:8080/lms/");
		
		assertEquals("ログイン | LMS", webDriver.getTitle());

		getEvidence(new Object() {
		}, "loginDisplay");
	}

}
