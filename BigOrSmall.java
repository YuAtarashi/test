package url;

public class BigOrSmall extends BetGame {


	//ゲームの名前
	public static final String gameName = "BigOrSmall";

	//ゲームのルール設定に必要なフィールド
	public static final int minBetPoint = 1;	//一度にBETできる下限
	public static final int maxBetPoint = 20;//一度にBETできる上限
	public static final int betRate = 2; //Winした時に獲得できるチップの掛け率
	public static final int cardDeckSize=52;	//カードデッキ１束にセットさせるカードの枚数

	//ゲームの進行上必要なフィールド
	private boolean betBigOrSmall;	//Bigに賭けるならtrue／Smallに賭けるならfalse


	//コンストラクタ
	public BigOrSmall(User user) {
		super(user);
		this.cardDeck = new Card[BigOrSmall.cardDeckSize];
		this.selectedCards = new Card[BigOrSmall.cardDeckSize];
		betBigOrSmall = true;
	}


	//以降のメソッドを使用してゲームを進行する
	@Override
	public void play() {

		BigOrSmall.introduct();//ゲームの導入・ルール説明
		System.out.println("ではゲームを始めましょう！" + "\n \n \n \n");

		 do{
			this.reset();//ゲームスタート状態に初期化
			this.drawCard();//カードを１枚引く
			this.showNow();//現在のカードを表示する
			this.betChips( BigOrSmall.minBetPoint , BigOrSmall.maxBetPoint );//チップをBETする

			while(true) {
				this.bet();//BETする内容を決める（Bigならtrue/Smallならfalse)
				this.drawCard();//カードを１枚引く
				this.showDrawCard();//引いたカードを表示する
				this.winOrLose();//勝敗を判定し、Winの場合はチップを与える

				if(this.winOrLose) {//勝った時
					if(this.drawCnt == BigOrSmall.cardDeckSize) {//カードデッキから全て引いてしまっていないかチェックする
						System.out.println("全てのカードを引き終わりました\n続けてBETすることはできません");
						break;
					}
					else if(this.rebet()) {//続けてBETするか確認⇒trueならループ
					}else {
						break;//⇒false(続けてBETしない）
					}
				}else {//負けた時
					break;
				}
			}

			System.out.println( " \n 現在の保有チップ＞＞＞" + this.user.getChips().toString() + "\n");
			if(user.checkChips(BigOrSmall.minBetPoint)) {//チップが足りているチェック⇒trueの時はスルー
			}else {
				System.out.println( " \n チップが足りません \n ゲームを続けられません \n" );
				break;//⇒false(チップが足りない）
			}
		 }while(playNextGame(BigOrSmall.gameName));//ゲームを続けるか確認

		this.showGrade(BigOrSmall.gameName);//ゲームの結果を表示する
		System.out.println( BigOrSmall.gameName + "を終了します\n");
		System.out.println( "◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆\n\n");


	}


	//導入：ゲームの説明
	public static void introduct() {

		BetGame.showBonusTime();

		System.out.println(
				"【" + BigOrSmall.gameName + " のルール】" + "\n" +
				"初めにカードが一枚配られます" + "\n" +
				"次に引くカードが現在のカードより大きいか小さいかをBETします" + "\n" +
				"１度にBETできるチップは最大" +BigOrSmall.maxBetPoint + "ポイントまでです" + "\n \n" +
				"当たった場合は、BETしたチップの" + BigOrSmall.betRate*Game.bonusRate  +"倍のチップがもらえます" + "\n" +
				"外れた場合はBETしたチップを失います" + "\n \n" +
				"当たった場合は獲得したチップをBETして、続けてカードを引くこともできます" + "\n" +
				"（この場合は" +BigOrSmall.maxBetPoint + "ポイント以上でもBETすることができます）" + "\n" +
				"チップが無くなるとゲーム終了です" + "\n \n" );
		}

	//カードデッキを初期化し、ゲーム開始状態にする
	public void reset () {
		this.drawCnt = 0;
		this.betPoint = 0;
		//カードデッキの配列の添え字とカードのIDが一致するようにセットする
		for(int i =0; i<BigOrSmall.cardDeckSize; i++) {
			this.cardDeck[i]= new Card(i);
			this.selectedCards[i] = null;
		}
	}

	//カードデッキからランダムに１枚カードを引く
	@Override
	public void drawCard() {

		while(true) {
			int r = new java.util.Random().nextInt(BigOrSmall.cardDeckSize);

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

		System.out.println("************************ 現在のチップとカード ************************\n");
		System.out.println("保有しているチップ：" + chips.toString());

		if(this.betPoint>0) {
			System.out.println("BETするチップ：" + this.betPoint + "ポイント");
		}else {
		}

		System.out.println("現在のカード：" + this.selectedCards[this.drawCnt-1].toString());
		System.out.println("\n**********************************************************************\n");

	}


	/* BETする内容を決める
	 * Bigに賭ける時はbetBigOrSmall = true
	 * Smallに賭ける時はbetBigOrSmall = false
	*/
	@Override
	public void bet() {
		System.out.println("\n■BigまたはSmallにBETして下さい\n");

		this.showNow();

		System.out.println("[Big]現在のカードより大きい方にBETする⇒0");
		System.out.println("[Small]現在のカードより小さい方にBETする⇒1");
		betBigOrSmall = Input.selectEither();//0,1のいづれかを入力して選択させる

		this.betTimes++;//BETした回数の累計
	}


	//引いたカードを表示する
	@Override
	public void showDrawCard() {
		System.out.println("************************ " + BigOrSmall.gameName + " ********************************\n");
		System.out.println( "BETしたチップ:" + this.betPoint + "ポイント\n");
		if(betBigOrSmall == true) {
			System.out.println("あなたの選択:Big\n");
		}else {
			System.out.println("あなたの選択:Small\n");
		}
			System.out.print("現在のカード:");
			System.out.print(this.selectedCards[this.drawCnt-2].toString());
			System.out.println("\n");
			System.out.print("引いたカード:");
			System.out.print(this.selectedCards[this.drawCnt-1].toString());
			System.out.println("\n");
			System.out.println("****************************************************************");
			System.out.println("\n");
	}


	//勝敗を判定する
	@Override
	public void winOrLose() {
		//引いたカード(drawCnt-2)が現在のカード(drawCnt-1)より大きければtrue/小さければfalse
		boolean comparePower = this.selectedCards[this.drawCnt-2].compareID(this.selectedCards[this.drawCnt-1]);
		if(comparePower == betBigOrSmall) {
			this.win(BigOrSmall.betRate);//winした結果を表示、チップを与える
			this.winOrLose =  true;
		}
		else {
			this.lose();//loseした結果を表示
			this.winOrLose =  false;
		}
	}

	//続けてBETする
	public boolean rebet () {
		System.out.println( "■獲得した" + this.betPoint + "ポイントのチップで続けてカードを引きますか？");
		System.out.println("[Yes]⇒0");
		System.out.println("[N o]⇒1");

		//0,1のいづれかを入力して選択させる
		if(Input.selectEither()) {//Yesを選択した時
			this.chips.betChips(this.betPoint);	//BETしたポイントをチップから引く
			System.out.println(this.betPoint +"ポイントのチップをBETします");
			this.sumBetPoint += this.betPoint;//BETしたポイントの累計
			return true;

		}else {//Noを選択した時
			return false;
		}

	}



}
