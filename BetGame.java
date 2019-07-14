package url;

import java.util.InputMismatchException;

public abstract class BetGame extends Game {


	//ゲームのルール設定に必要なフィールド
	protected int minBetPoint;		//一度にBETできる下限
	protected int maxBetPoint;		//一度にBETできる上限
	protected int betRate;			//Winした時に獲得できるチップの掛け率

	//ゲームの進行上必要なフィールド
	protected Card[] selectedCards;//ゲームの中で引かれたカードインスタンスの配列
	protected int drawCnt;			//連続してカードを引いた回数(配列selectedCardsの添え字）
	protected int betChip10;		//BETする10ポイントチップの枚数
	protected int betChip1;		//BETする1ポイントチップの枚数
	protected int betPoint;		//BETするポイント数
	protected boolean winOrLose;	//ゲームの勝敗

	//ゲームの成績に関するフィールド
	protected double betTimes;		//BETした回数
	protected double sumBetPoint;	//BETしたチップの累計
	protected double winTimes;		//Winした回数
	protected double sumWinPoint;	//獲得したチップの累計



	//コンストラクタ
	public BetGame(User user) {
		super(user);

		this.drawCnt = 0;
		this.betChip10 = 0 ;
		this.betChip1 = 0 ;
		this.betPoint = 0 ;
		this.betTimes = 0 ;
		this.winOrLose = true;

		this.betTimes = 0;
		this.sumBetPoint = 0 ;
		this.winTimes = 0 ;
		this.sumWinPoint = 0 ;
	}


	//bonusGameで掛け率が変更されている時にintroductメソッドで表示させる
	public static void showBonusTime() {
		if(Game.bonusRate > 1) {
			System.out.println("★ボーナスタイム中★掛け率が" + Game.bonusRate + "倍です！\n");
		}else {
		}
	}

	//現在のカードを表示
	public abstract void showNow();


	//チップをBETする（BETするポイントを入力→betPointをセット→チップからbetPointwを引く）
	public void betChips(int minBetPoint,int maxBetPoint) {

		int chip10 = this.chips.getChip10();
		int chip1 = this.chips.getChip1();
		int point = chip10*10 + chip1;

		while(true) {
			try {
				System.out.println(" \n■BETするチップをポイント数で入力して下さい（最低" + minBetPoint + "～最大" + maxBetPoint + "）");
				int input = new java.util.Scanner(System.in).nextInt();

				if(input > point) {
					throw new InputMismatchException
					("BETするチップポイントは保有チップ量以下で入力してください \n 入力値："+ input
							+ "\n 現在の保有チップポイント：" + point + "\n" );
				}else if(input<minBetPoint || input>maxBetPoint) {
					throw new InputMismatchException
					("BETできるポイントは" + minBetPoint + "～" + maxBetPoint + "ポイントです \n 入力値:" + input );
				}else if( input%10 > chip1) {
					throw new InputMismatchException
					("保有チップ単位でしかBETできません \n 入力値:" + input +"\n"
							+ "現在1ポイントチップの保有枚数は" + chip1 + "枚です");
				}else {
					this.betPoint = input;
					System.out.println( ">>>" + this.betPoint + "ポイントをBETします \n");
					chips.betChips(this.betPoint);	//BETしたポイントをチップから引く

					this.sumBetPoint += this.betPoint;	//BETしたポイントの累計

					break;
				}
			}catch(InputMismatchException  e){
				System.out.println( "入力エラーです \n " + e.getMessage());
				System.out.println( "保有チップ枚数以下の" + minBetPoint + "～" + maxBetPoint + "までの半角数字で入力して下さい" );
			}
		}
	}


	//BETする内容を決める
	public abstract void bet();


	//winした結果を表示、チップを与える
	public void win(int betRate) {
		System.out.println("★★★★★★★★");
		System.out.println("★★★Win!★★★");
		System.out.println("★★★★★★★★");
		this.betPoint *= betRate*Game.bonusRate;	//Winした時に得られるポイント
		this.chips.winChips(this.betPoint);//獲得したポイントをチップに足す
		System.out.println( " \n チップ" + this.betPoint + "ポイントを獲得しました！ \n" );

		this.winTimes++;					//Winした回数の累計
		this.sumWinPoint += this.betPoint;	//獲得したポイントの累計
	}

	//loseした結果を表示
	public void lose() {
		System.out.println("▼▼▼▼▼▼▼▼");
		System.out.println("▼▼Lose...▼▼");
		System.out.println("▼▼▼▼▼▼▼▼\n");
	}


	//次のゲームに進むかを選択する
	public boolean playNextGame(String gameName) {
		System.out.println("■" + gameName + "のゲームを続けますか？");
		System.out.println("[Yes]⇒0");
		System.out.println("[N o]⇒1");

		if(Input.selectEither()) {//0または１を入力して選択させる（0ならtrue，1ならfalse）
			System.out.println("Let's play next game!\n");
			return true;

		}else {
			return false;
		}
	}

	//ゲームの結果を表示する
	public void showGrade(String gameName) {
		double winRate = this.winTimes/this.betTimes*100;
		double winPointRate = (this.sumWinPoint - this.sumBetPoint)/this.sumBetPoint*100;

		System.out.println( "\n****************************************************************************");
		System.out.println( "**************************" + gameName + "の成績" + "******************************\n" );

		System.out.println( "BETした回数 : " + String.format("%.0f", this.betTimes) + "回" );
		System.out.println( "Winした回数 : " + String.format("%.0f", this.winTimes) + "回" );
		System.out.println( "勝率 : " + String.format("%.1f", winRate)+ "%"  );

		System.out.println( "BETしたトータルチップ : " + String.format("%.0f", this.sumBetPoint) + "ポイント"  );
		System.out.println( "獲得したトータルチップ : " + String.format("%.0f", this.sumWinPoint)  + "ポイント"   );
		System.out.println( "チップ獲得率 : " + String.format("%.1f", winPointRate) + "%"  );

		System.out.println( "現在の保有チップ : " + this.chips.toString());

		System.out.println( "\n****************************************************************************");
		System.out.println( "****************************************************************************\n");
	}


}
