package url;


public abstract class Game {


	protected String gameName;//ゲーム名
	protected User user;//プレイするユーザー
	protected String userName;//ユーザーの名前
	protected Chips chips;//ユーザーの持つチップ
	protected int cardDeckSize;	//カードデッキのカードの枚数
	protected Card[] cardDeck;//カードデッキ（ゲームに使用するカードの束）
	protected static int bonusRate;	//bonusGameでWinした時にUPするゲームの掛け率



	public Game(User user) {
		this.user = user;
		this.userName = user.getUserName();
		this.chips = user.getChips();
	}


	//bonusRateのセッターメソッド
	public static void setBonusRate(int bonusRate) {
		Game.bonusRate = bonusRate;
	}

	//ゲームの進行
	public abstract void play() ;

	//カードを１枚引く
	public abstract void drawCard() ;

	//引いたカードを表示する
	public abstract void showDrawCard() ;

	//ゲームの勝敗を判定する
	public abstract void winOrLose() ;



}
