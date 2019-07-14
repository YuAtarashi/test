package url;

public class User {

	private String userName;//ユーザー名
	private Chips chips;//ユーザーが保有するチップ
	private BetGame [] playedGames;//チャレンジしたBETゲーム
	private BonusGame bonusGame;//チェレンジするボーナスゲーム


	//コンストラクタ
	public User(String userName) {
		this.userName = userName;
		this.chips = new Chips();
		this.playedGames = new BetGame[Play.gameTimes];
		this.bonusGame = new BonusGame(this);
	}

	/*BetGameにチャレンジする
	 *j番目のチャレンジでゲームナンバーgameNum番のBetGameにチャレンジする
	 *配列playedGames[j]にBetGameをインスタンス化して代入⇒playメソッドを実行する
	 */
	public void playBetGame(int j ,int gameNum) {
		switch(gameNum) {
		case 0 : this.playedGames[j] = new BigOrSmall(this);
				  break;
		case 1 : this.playedGames[j] = new GuessCard(this);
		  		  break;
		case 2 : this.playedGames[j] = new PairCards(this);
		  		  break;
		}
		this.playedGames[j].play();
	}


	//BonusGameにチャレンジする
	public void playBonusGame() {
		this.bonusGame.play();
	}

	//保有点をチェックし、ゲームを続けられるか確認する
	public boolean checkChips(int minBetPoint) {
		if(this.chips.getPoint() >= minBetPoint) {
			return true;//ゲーム続行可
		}
		else {
			return false;//ゲーム続行不可
		}
	}


	//チャレンジ終了後のユーザーの成績を表示する
	public void showTotalGrade(String[] betGameName , int[] betGameCnt) {

		double betTimes = 0 ;//BETした回数
		double sumBetPoint = 0 ;//BETした総チップポイント
		double winTimes = 0 ;//WINした回数
		double sumWinPoint = 0 ;//WINした総チップポイント

		for(int i = 0; i<playedGames.length ; i++) {
			if(this.playedGames[i] != null) {
				betTimes += this.playedGames[i].betTimes;
				sumBetPoint += this.playedGames[i].sumBetPoint;
				winTimes += this.playedGames[i].winTimes;
				sumWinPoint += this.playedGames[i].sumWinPoint;
			}else {//nullの時はスルー
			}
		}

		double winRate = winTimes/betTimes*100;//勝率%
		double winPointRate = (sumWinPoint - sumBetPoint)/sumBetPoint*100;//チップ獲得率%

		System.out.println( "\n****************************************************************************");
		System.out.println( "**************************" + this.userName + "さんの成績" + "******************************\n" );

		System.out.println( "チャレンジしたゲーム" );

		for(int i = 0; i<Play.gameKinds ; i++) {
			System.out.println( betGameName[i] + " : " + betGameCnt[i]+ "回");
		}

		System.out.println( BonusGame.gameName + "の結果 :" + bonusGame.getGrade() );

		System.out.println( "BETした回数 : " + String.format("%.0f", betTimes) + "回" );
		System.out.println( "Winした回数 : " + String.format("%.0f", winTimes) + "回" );
		System.out.println( "勝率 : " + String.format("%.1f", winRate)+ "%"  );

		System.out.println( "BETしたトータルチップ : " + String.format("%.0f", sumBetPoint) + "ポイント"  );
		System.out.println( "獲得したトータルチップ : " + String.format("%.0f", sumWinPoint)  + "ポイント"   );
		System.out.println( "チップ獲得率 : " + String.format("%.1f", winPointRate) + "%"  );

		System.out.println( "最終チップ数 : " + this.chips.toString() );

		System.out.println( "\n****************************************************************************");
		System.out.println( "****************************************************************************\n");
	}


	//チャレンジ終了後のユーザーの保有チップを表示する
	public void showLastChips() {
		System.out.println( this.userName + "さん／最終チップ数 : " + this.chips.toString());
	}


	//ユーザー名のゲッターメソッド
	public String getUserName() {
		return this.userName;
	}

	//ユーザーが保有するチップインスタンスのゲッターメソッド
	public Chips getChips() {
		return this.chips;
	}

	//ユーザーが保有するチップポイントのゲッターメソッド
	public int getChipsPoint() {
		return this.chips.getPoint();
	}


}
