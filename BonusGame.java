package url;

public class BonusGame extends Game {

	//ゲームの名前
	public final static String gameName = "BounusGame";//ゲーム名(
	public final static int minBetPoint = 10;//ゲームにチャレンジするために必要な最低点

	//ゲームのルール設定に必要なフィールド
	//カードデッキのカードの枚数
	public final static int cardDeckSize = 53;
	//カードの数字の範囲(ゲームの結果判定に使用）
	public final static int rangeA = 1;
	public final static int rangeB = 6;
	public final static int rangeC = 13;
	//Winした時にUPするゲームの掛け率
	public final static int bonusRate1 = 2;
	public final static int bonusRate2 = 4;
	//Winした時に獲得するポイント
	public final static int bonusPoint1 = 50;
	public final static int bonusPoint2 =100;
	//Loseした時に没収されるポイントの割合(%)
	public final static int loseRate = 50;
	//Loseした時に没収されるポイント
	public final static int losePoint = minBetPoint;

	//ゲームの進行上必要なフィールド
	private Card drawCard;//ゲーム中にカードデッキから引いたカードインスタンス
	private String grade;//ゲームの結果


	//コンストラクタ
	//idが0～52までのジョーカーを含む53枚のカードがセットされたカードデッキをインスタンス化
	public BonusGame(User user) {
		super(user);
		this.cardDeck = new Card[BonusGame.cardDeckSize];
		for(int i =0; i<BonusGame.cardDeckSize; i++) {
			this.cardDeck[i]= new Card(i);
		}
		this.grade = "Not Challenge";
	}

	//ゲームの進行
	@Override
	public void play() {
		System.out.println("ここでチャンスタイム！" );
		System.out.println(BonusGame.gameName + "にチャレンジできます" );

		BonusGame.introduct();

		System.out.println(BonusGame.gameName + "にチャレンジしますか？" );
		System.out.println("[Yes]⇒0");
		System.out.println("[N o]⇒1");
		if(Input.selectEither()) {//0,1のいづれかを入力して選択させる
			this.drawCard();
			this.showDrawCard();
			this.winOrLose();
		}
		else {
			System.out.println(BonusGame.gameName + "にチャレンジせずに次のゲームに進みます" );
		}
		System.out.println( "◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆\n\n");

	}

	//ゲームの導入・ルール説明
	public static void introduct() {
		System.out.println(
				"【" + BonusGame.gameName + " のルール】" + "\n" +
				"ジョーカーを含む" + BonusGame.cardDeckSize + "枚の1組のトランプから１枚のカードを引きます" + "\n\n" +
				"◆引いたカードが赤色の" + BonusGame.rangeA+1 + " ～" +BonusGame.rangeB + "の時" + "\n" +
				"　　次にチャレンジするゲームの掛け率が" + BonusGame.bonusRate1 + "倍になります" + "\n\n" +
				"◆引いたカードが赤色の" + BonusGame.rangeB+1 + "～" + BonusGame.rangeC + "の時" + "\n" +
				"　　次にチャレンジするゲームの掛け率が" + BonusGame.bonusRate2 + "倍になります" + "\n\n" +
				"◆引いたカードが黒色の" + BonusGame.rangeA+1 + "～" + BonusGame.rangeB + "の時" + "\n" +
				"　　チップが" + BonusGame.bonusPoint1 + "ポイントもらえます" + "\n\n" +
				"◆引いたカードが黒色" + BonusGame.rangeB+1 + "～" + BonusGame.rangeC + "の時" + "\n" +
				"　　チップが" + BonusGame.bonusPoint2 + "ポイントもらえます" + "\n\n" +
				"◆引いたカードが" + BonusGame.rangeA + "の時" + "\n" +
				"　　手持ちのチップから" + BonusGame.losePoint + "ポイントが没収になります" + "\n\n" +
				"◆引いたカードがジョーカーの時" + "\n" +
				"　　手持ちのチップの" + BonusGame.loseRate + "%が没収になります" + "\n\n" );
	}

	//カードを１枚引く
	@Override
	public void drawCard() {
		int r = new java.util.Random().nextInt(this.cardDeckSize);
		this.drawCard = this.cardDeck[r];//ランダム数r=カードID
	}


	//引いたカードを表示する
	@Override
	public void showDrawCard() {
		System.out.println("あなたの引いたカード：" + this.drawCard.toString() );
	}

	//ゲームの勝敗を判定する
	@Override
	public void winOrLose() {
		int id = this.drawCard.getID();
		int markNum = this.drawCard.getMarkNum();
		int num = this.drawCard.getNum();

		if(id == 52) {//ジョーカーの時
			int lose =  this.chips.getPoint() * BonusGame.loseRate / 100 ;
			this.chips.betChips(lose);
			System.out.println("残念！" + lose + "ポイント没収です..." );
			this.grade = "Lose<" + lose + "ポイントの没収>";

		}else if(num == BonusGame.rangeA) {
			this.chips.betChips(BonusGame.losePoint);
			System.out.println("残念！" + BonusGame.losePoint + "ポイント没収です..." );
			this.grade = "Lose<" + BonusGame.losePoint + "ポイントの没収>";

		}else if(markNum == 1 || markNum == 2 ) {//赤いカードの時
			if(num > BonusGame.rangeA && num <= BonusGame.rangeB ) {
				Game.bonusRate = BonusGame.bonusRate1;
				System.out.println("おめでとう！ゲームの掛け率が" + BonusGame.bonusRate1 + "倍になります！" );
				this.grade = "Win<掛け率" + BonusGame.bonusRate1 + "倍>";

			}else{
				Game.bonusRate = (BonusGame.bonusRate2);
				System.out.println("おめでとう！ゲームの掛け率が" + BonusGame.bonusRate2 + "倍になります！" );
				this.grade = "Win<掛け率" + BonusGame.bonusRate2 + "倍>";
			}

		}else{//黒いカードの時
			if(num > BonusGame.rangeA+1 && num < BonusGame.rangeB ) {
				this.chips.winChips(BonusGame.bonusPoint1);
				System.out.println("おめでとう！" + BonusGame.bonusPoint1 + "ポイント獲得です！" );
				this.grade = "Win<" + BonusGame.bonusPoint1 + "ポイント獲得>";

			}else{
				this.chips.winChips(BonusGame.bonusPoint2);
				System.out.println("おめでとう！" + BonusGame.bonusPoint2 + "ポイント獲得です！" );
				this.grade = "Win<" + BonusGame.bonusPoint2 + "ポイント獲得>";
			}
		}
	}


	//ゲーム結果のゲッターメソッド
	public String getGrade() {
		return this.grade;
	}




}
