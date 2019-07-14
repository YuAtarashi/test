package url;

public class PairCards extends BetGame {


	//ゲームの名前
	public static final String gameName = "PairCards";

	//ゲームのルール設定に必要なフィールド
	public static final int minBetPoint = 10;//一度にBETできる下限
	public static final int maxBetPoint = 13;//一度にBETできる上限
	public static final int betRate = 10;//Winした時に獲得できるチップの掛け率
	public static final int cardDeckSize=20;//カードデッキ１束にセットさせるカードの枚数

	//ゲームの進行上必要なフィールド
	private Card winedCards[] = new Card [PairCards.cardDeckSize];//ペアを組んで獲得したカード
	private int betCard[] = new int[2];//選択したカードの番号


	//コンストラクタ
	public PairCards(User user) {
		super(user);
		this. betPoint = 10;
		this.cardDeck = new Card[PairCards.cardDeckSize];
		this.selectedCards = new Card[PairCards.cardDeckSize];

		//カードデッキにランダムな順番でカードをセットする
		for(int i =0 ; i<4 ; i++) {	//マークナンバー0～3
			for(int j = 1 ; j<6 ; j++) {//数字1～5
				while(true) {
					int r = new java.util.Random().nextInt(PairCards.cardDeckSize);
					if(this.cardDeck[r] == null) {
						this.cardDeck[r]= new Card(j,i);
						break;
					}
				}
			}
		}
	}


	//以降のメソッドを使用してゲームを進行する
	@Override
	public void play() {

		PairCards.introduct();
		System.out.println("ではゲームを始めましょう！" + "\n \n \n \n");

		 do{
			this.showNow();//現在のカードを表示
			this.betChips( PairCards.minBetPoint ,PairCards.maxBetPoint );//チップをBETする
			this.bet();//BETする(カードを２枚選択する）
			this.showDrawCard();//引いたカードを表示する
			this.winOrLose();//勝敗を判定し、Winの場合はチップを与える

			if(this.winOrLose) {//勝った時はループ
			}else{	//負けた時
				System.out.println( " \n 現在の保有チップ＞＞＞" + this.user.getChips().toString() + "\n");
				if(user.checkChips(PairCards.minBetPoint)) {//チップが足りている時はループ
				}else {//チップが足りていない時はループを抜ける
					System.out.println( " \n チップが足りません \n ゲームを続けられません \n" );
					break;
				}
			}

			if(this.winTimes == PairCards.cardDeckSize/2) {
				//全てのカードがペアになっていないかチェック⇒true（全てペア）の時はbreak
				System.out.println("全てのカードを引き終わりました\nチャレンジ終了です");
				break;
			}else{//⇒false(ペアになっていないカードがある⇒ループ）
			}


		 }while(playNextGame(PairCards.gameName));//ゲームを続ける選択

		this.showGrade(PairCards.gameName);
		System.out.println( PairCards.gameName + "を終了します\n");//ゲームの結果を表示する
		System.out.println( "◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆\n\n");

	}


	//導入：ゲームの説明
	public static void introduct() {
		BetGame.showBonusTime();

		System.out.println(
				"【" + PairCards.gameName + " のルール】" + "\n" +
				"各マークの１～５まで、" + PairCards.cardDeckSize + "枚のトランプが伏せて並べてあります" + "\n" +
				"そこから２枚のカードを選び、選択した２枚のカードが同じ数字であればカードの数字の" + PairCards.betRate*Game.bonusRate + "倍のチップがもらえます" + "\n" +
				"外れた場合は" + PairCards.minBetPoint + "ポイントを失い、カードは元に戻します" + "\n" +
				"チップが無くなるか、すべてのカードを引くまでゲームを続けることができます"+ "\n\n\n");
		}


	//現在のカードを表示
	@Override
	public void showNow() {

		System.out.println("***************************** 現在のチップ *****************************\n");
		System.out.println("保有のチップ：" + chips.toString());
		System.out.println("\n\n");

		for(int i = 0 ; i < PairCards.cardDeckSize ; i++ ) {
			System.out.print("[" + (i+1) + "]");
			if(this.winedCards[i] != null) {
				System.out.print("Pair!");
				System.out.println(this.winedCards[i].toString());
			}
			else if(this.selectedCards[i] != null) {
				System.out.println("OPENED");
			}else {
				System.out.println("??????");
			}
		}
		System.out.println("\n**********************************************************************\n");

	}


	//引くカードを２枚選択する
	@Override
	public void drawCard() {
		for( int i = 0; i<2; i++){
			while(true) {
				System.out.println( "■"+ (i+1)  +"枚目のカードを選んでください");
				this.betCard[i] = Input.selectWhichOne(1,PairCards.cardDeckSize)-1;//1～19で入力して選択させる
				if(i == 1 && this.betCard[0] == this.betCard[1]) {//2枚目を引く時に１枚目と２枚目が同じならば
					System.out.println( "1枚目と同じカードは選択できません。他のカードを選択して下さい");
				}else if(winedCards[this.betCard[i]] == null) {
					this.selectedCards[this.betCard[i]] = this.cardDeck[betCard[i]];
					System.out.println(">>>" + this.selectedCards[this.betCard[i]].toString());
					this.drawCnt ++;
					break;
				}else {
					System.out.println( "獲得済みのカードです。他のカードを選択して下さい");
				}
			}
		}
	}

	//BETする
	@Override
	public void bet() {
		this.drawCard();//引くカードを２枚選択する
		this.betTimes ++ ;
	}


	//チップをBETする
	@Override
	public void betChips( int minBetPoint ,int maxBetPoint ) {
		this.betPoint = 10;
		this.chips.betChips(this.betPoint);
		this.sumBetPoint += this.betPoint;
	}


	//引いたカードを表示する
	@Override
	public void showDrawCard() {
		System.out.println("************************ " + PairCards.gameName + " ********************************\n");
		for(int i = 0 ; i< 2 ; i++) {
			System.out.print( "\n"  + (i+1)  + "枚目のカード : ");
			System.out.println("[" + (this.betCard[i]+1) + "]" + this.selectedCards[betCard[i]].toString());
		}
		System.out.println("\n");
		System.out.println("****************************************************************");
		System.out.println("\n");
	}


	//勝敗を判定し、Winの場合はチップを与える
	@Override
	public void winOrLose() {
		if(this.selectedCards[betCard[0]].equalsNum(selectedCards[betCard[1]])){
			this.betPoint = this.selectedCards[betCard[0]].getNum();
			for(int i = 0 ; i<2 ; i++) {
				this.winedCards[betCard[i]] = this.selectedCards[betCard[i]];
			}
			this.win(PairCards.betRate);//winした結果を表示、チップを与える
			System.out.println( " \n 現在の保有チップ＞＞＞" + chips.toString() + "\n");
			winOrLose = true;
		}
		else {
			this.betPoint = PairCards.minBetPoint;
			this.lose();//loseした結果を表示
			winOrLose = false;
		}
	}






}
