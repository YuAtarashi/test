package url;

public class GuessCard extends BetGame {

	//ゲームの名前
	public static final String gameName = "GuessCard";

	//ゲームのルール設定に必要なフィールド
	public static final int minBetPoint = 1;//一度にBETできる下限
	public static final int maxBetPoint = 30;//一度にBETできる上限
	public static final int cardDeckSize=52;	//カードデッキ１束にセットさせるカードの枚数
	public static final int betRateMark = 5; //マークにBETしてWinした時に獲得できるチップの掛け率
	public static final int betRateNum = 15;	//数字ににBETしてWinした時に獲得できるチップの掛け率
	public static final int betRateID = 50;	//マークと数字両方ににBETしてWinした時に獲得できるチップの掛け率

	//ゲームの進行上必要なフィールド
	private int betType;	//BETする対象（0：マーク／1：数字／2：マークと数字両方）
	private int betNum;	//BETしたカードのマーク
	private int betMark;	//BETしたカードの数字
	private Card betID;		//BETしたIDを持つカードインスタンス


	//コンストラクタ
	public GuessCard(User user) {
			super(user);
			this.betType = 1;
			this.cardDeck = new Card[GuessCard.cardDeckSize];
			this.selectedCards = new Card[GuessCard.cardDeckSize];

			//カードデッキの配列の添え字とカードのIDが一致するようにセットする
			for(int i =0; i<GuessCard.cardDeckSize; i++) {
				this.cardDeck[i]= new Card(i);
			}

		}


	//以降のメソッドを使用してゲームを進行する
	@Override
	public void play() {

		GuessCard.introduct();//導入：ゲームの説明
		System.out.println("ではゲームを始めましょう！" + "\n \n \n \n");

		 do{
			this.showNow();//現在のカードを表示
			this.selectBetType();//BETすつ対象を選ぶ
			this.betChips( GuessCard.minBetPoint , GuessCard.maxBetPoint );//チップをBETする
			this.bet();//BETする(BETする対象に合わせて、BETする内容を決める）
			this.drawCard();//カードを１枚引く
			this.showDrawCard();//引いたカードを表示する
			this.winOrLose();//勝敗を判定し、Winの場合はチップを与える

			if(this.winOrLose) {//勝った時はスルー
			}else{//負けた時
				System.out.println( " \n 現在の保有チップ＞＞＞" + this.user.getChips().toString() + "\n");
				if(user.checkChips(GuessCard.minBetPoint)) {//チップが足りているチェック⇒trueの時はスルー
				}else {//⇒false(チップが足りない）
					System.out.println( " \n チップが足りません \n ゲームを続けられません \n" );
					break;
				}
		 	}

			if(this.drawCnt == GuessCard.cardDeckSize) {
				//カードデッキから全て引いてしまっていないかチェック⇒true（カードデッキが空）の時はbreak
				System.out.println("全てのカードを引き終わりました\nチャレンジ終了です");
				break;
			}else{//⇒false(カードデッキが空ではない⇒ループ）
			}

		 }while(this.playNextGame(GuessCard.gameName));//ゲームを続けるか確認

		this.showGrade(GuessCard.gameName);//ゲームの結果を表示する
		System.out.println( GuessCard.gameName + "を終了します\n");
		System.out.println( "◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆\n\n");

	}


	//導入：ゲームの説明
	public static void introduct() {
		BetGame.showBonusTime();

		System.out.println(
				"【" + GuessCard.gameName + " のルール】" + "\n" +
				"ジョーカーを除く５２枚のトランプから次に引くカードを当てるゲームです" + "\n" +
				"BETする方法は３種類あり、当たった時の掛け率が異なります" + "\n\n" +
				"①マークにBETする⇒\n"
				+ "当たった場合はBETしたチップの"+ GuessCard.betRateMark*Game.bonusRate + "倍のチップがもらえます" + "\n\n" +
				"②数字にBETする⇒\n"
				+ "当たった場合はBETしたチップの"+ GuessCard.betRateNum*Game.bonusRate + "倍のチップがもらえます" + "\n\n" +
				"③マークと数字両方にBETする⇒\n"
				+ "当たったはBETしたチップの"+ GuessCard.betRateID*Game.bonusRate + "倍のチップがもらえます" + "\n\n" +
				"外れた場合はBETしたチップを失います" + "\n\n" +
				"一度にBETできるチップは" + GuessCard.maxBetPoint + "ポイント分までです" + "\n" +
				"チップが無くなるか、すべてのカードを引くまでゲームを続けることができます" + "\n\n\n");
		}


	//カードデッキからランダムに１枚カードを引く
	@Override
	public void drawCard() {

		while(true) {
			int r = new java.util.Random().nextInt(GuessCard.cardDeckSize);

			if(this.cardDeck[r] != null) {
				this.selectedCards[this.drawCnt] = new Card(r); //drawCnt番目に引いたカード＝IDがｒのカード
				this.cardDeck[r] = null;//cardDeckの配列番号とIDは一致している⇒IDがrのカードをcardDeckから抜く
				break;
			}else {
			}
		}
		this.drawCnt++;

	}

	//現在のカードを表示
	@Override
	public void showNow() {

		System.out.println("***************************** 現在のチップ *****************************\n");
		System.out.println("保有のチップ：" + chips.toString());
		System.out.println("\n**********************************************************************\n");

	}

	//BETすつ対象を選ぶ
	public void selectBetType() {
		System.out.println(" \n■BETする対象を選んでください");
		System.out.println("[マーク] ⇒ 0");
		System.out.println("[数字] ⇒ 1");
		System.out.println("[マークと数字両方] ⇒ 2");
		this.betType = Input.selectWhichOne(0,2);//0～2で入力して選択させる

		this.betTimes ++;
		}


	//BETする
	@Override
	public void bet() {
		switch(this.betType) {
		case 0 : this.betMark();//マークにBETする(クラブ：0/ダイヤ：1/ハート：2/スペード：3）
				  break;
		case 1 : this.betNum();//数字にBETする（1～13）
		 		  break;
		case 2 : this.betID();//マーク番号と数字をIDに変換すし、BETしたIDのカードをインスタンス化
				 break;
		}
	}

	//マークにBETする
	public void betMark() {
		System.out.println("\n■BETするマークを選んでください\n");
		System.out.println("[クラブ] ⇒ 0");
		System.out.println("[ダイヤ] ⇒ 1");
		System.out.println("[ハート] ⇒ 2");
		System.out.println("[スペード] ⇒ 3");
		this.betMark = Input.selectWhichOne(0,3);//0～3で入力して選択させる
	}

	//数字にBETする
	public void betNum() {
		System.out.println("\n■BETする数字を入力して下さい\n");
		this.betNum =  Input.selectWhichOne(1,13);//1～13で入力して選択させる
	}

	//マークと数字両方にBETする(id = markNum + (num-1)*4)
	public void betID() {
		this.betMark();//マークにBETする(クラブ：0/ダイヤ：1/ハート：2/スペード：3）
		this.betNum();//数字にBETする（1～13）
		int betID = (this.betMark) + (this.betNum-1)*4;//マーク番号と数字をIDに変換する
		this.betID = new Card(betID);//BETしたIDのカードをインスタンス化
	}


	//引いたカードを表示する
	@Override
	public void showDrawCard() {
		System.out.println("************************ " + GuessCard.gameName + " ********************************\n");
		System.out.println( "BET数:" + this.betPoint + "ポイント\n");
		switch(this.betType) {
		case 0:
			System.out.println("BETの対象:マーク\n");
			System.out.println("掛け率:×" + GuessCard.betRateMark + "\n");
			switch(this.betMark) {
			case 0:
				System.out.println("BETしたマーク:クラブ\n");
				break;
			case 1:
				System.out.println("BETしたマーク:ダイヤ\n");
				break;
			case 2:
				System.out.println("BETしたマーク:ハート\n");
				break;
			case 3:
				System.out.println("BETしたマーク:スペード\n");
				break;
			}
			break;
		case 1:
			System.out.println("BETの対象:数字\n");
			System.out.println("掛け率:×" + GuessCard.betRateNum + "\n");
			System.out.println("BETした数字:" + this.betNum + "\n");
			break;
		case 2:
			System.out.println("BETの対象:マークと数字の両方\n");
			System.out.println("掛け率:×" + GuessCard.betRateID + "\n");
			System.out.println("BETした数字とマーク:" + this.betID.toString() + "\n");
			break;
		}
			System.out.print("引いたカード:");
			System.out.print(this.selectedCards[this.drawCnt-1].toString());
			System.out.println("\n");
			System.out.println("****************************************************************");
			System.out.println("\n");
	}


	//勝敗を判定し、Winの場合はチップを与える
	@Override
	public void winOrLose() {
		boolean compare = true;
		switch(this.betType) {
		case 0 : compare = this.selectedCards[this.drawCnt-1].getMarkNum() == this.betMark;
				this.betRate = GuessCard.betRateMark;
				break;
		case 1 : compare = this.selectedCards[this.drawCnt-1].getNum() == this.betNum;
				this.betRate = GuessCard.betRateNum;
				break;
		case 2 : compare = this.betID.equals(this.selectedCards[this.drawCnt-1]);
				this.betRate = GuessCard.betRateID;
				break;
		}
		if(compare)  {
			this.win(this.betRate);//winした結果を表示、チップを与える
			this.winOrLose =  true;
		}
		else {
			this.lose();//loseした結果を表示
			this.winOrLose =  false;
		}
	}



}
