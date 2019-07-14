package url;

import java.util.Arrays;

public class Play {


	//★ゲームのルール設定に必要なフィールド
	public static final int maxUser = 5;//チャレンジできるユーザーの人数

	public static final int gameKinds = 3;//チャレンジできるBETゲームの種類
	public static final int gameTimes = 5;//１ユーザーがチャレンジできるゲームの回数
	public static final int maxGameTimes = 2; //１種類のゲームにチャレンジできる回数
	public static final int firstHalf = 3;//前半（チャンスタイムまで）にチャレンジするゲームの回数

	public static final int firstChip10 = 8;//ゲーム開始時点の10ポイントチップの枚数
	public static final int firstChip1 = 20;//ゲーム開始時点の1ポイントチップの枚数

	//BETゲームの名前
	public static final String [] betGameName = {BigOrSmall.gameName,GuessCard.gameName,PairCards.gameName};
	//各ゲームにチャレンジするのに必要なチップ
	public static final int [] minBetPoint = {BigOrSmall.minBetPoint,GuessCard.minBetPoint,PairCards.minBetPoint};


	//★ゲームの進行上必要なフィールド
	/*各ゲームにチャレンジした回数（添え字はbetGameNameに一致させる）
	 * betGameCnt[0] ⇒ BigOrSmallにチャレンジした回数
	 * betGameCnt[1] ⇒ GuessCardにチャレンジした回数
	 * betGameCnt[2] ⇒ PairCardsにチャレンジした回数
	 */
	private static int [] betGameCnt = new int [gameKinds];

	//チャレンジするユーザー
	private static User user[] = new User [maxUser];


	public static void main(String[] args) {

		int cntUser = 0;

		Play.introduct();//導入・ルールの説明

		outside1 : while(true) {

			for( int i = 0; i<user.length ; i++) {

				//ユーザー名を入力してインスタンス化
				String userName = Play.inputUserName();
				user[i] = new User(userName);
				System.out.println(userName + "さん、こんにちは！");
				System.out.println("ゲームを始めましょう！" + "\n \n");
				cntUser++;

				//カウントの初期化
				for(int j =0; j<gameKinds ; j++) {
					betGameCnt[j] = 0;
				}
				//bonusRateを１に設定
				Game.setBonusRate(1);


				/*BetGame３回⇒BonusGame１回⇒BetGame２回
				 *チップが無くなると途中で終了
				 *チャレンジを続行するかは選択できる
				*/
				outside2 : while(true) {

					//前半:BetGameのいづれかにチェレンジ
					for( int j = 0; j<firstHalf ; j++) {
						/*チャレンジするBetGameを一つ選択
						 *選択したBetGameにチャレンジ
						 * チャレンジ後のチップをチェックし、次のゲームにチャレンジできるか判断する
						 * チャレンジ続行：可⇒true ／不可⇒false
						*/
						if(Play.playBetGame(user,i,j)) {
						}else {
							break outside2;
						}
						//チャレンジを続行するか確認する
						if(Play.playNextBetGame(user,i)) {
						}else {
							break outside2;
						}
					}

					//チャンスタイム・BonusGameにチャレンジ
					if(user[i].checkChips(BonusGame.minBetPoint)){
						user[i].playBonusGame();
					}else {//チャレンジするために必要なチップが足りない時
					}

					//後半:BetGameのいづれかにチャレンジ
					for( int j = firstHalf ; j<gameTimes ; j++) {
						if(Play.playBetGame(user,i,j)) {
						}else {
							break outside2;
						}
						if(Play.playNextBetGame(user,i)) {
						}else {
							break outside2;
						}
					}

					break;
				}

				//ユーザーの成績を表示
				user[i].showTotalGrade(betGameName , betGameCnt);

				if(i == user.length -1) {//最後のループ
					break outside1;
				}else {//最後のループ以外
					System.out.println("他のユーザーもチャレンジしますか？");
					System.out.println("[Yes]⇒0");
					System.out.println("[N o]⇒1");
					//0,1のいづれかを入力して選択させる
					if(Input.selectEither()) {//yesの時⇒ループ
					}else {//Noの時
						break outside1;
					}
				}
			}
		}


		//最終保有チップが多い順に添え字0～cntUser-1までの配列をソートする(ラムダ式）
		Arrays.sort(user,0,cntUser,(a,b)-> b.getChipsPoint() - a.getChipsPoint());

		//チャレンジした全ユーザーの成績をまとめて表示
		System.out.println("★★★ユーザーの成績発表★★★");

		for( int i = 0; i<user.length ; i++) {
			if(user[i] != null) {
				System.out.println( "【" + (i+1) + "位】" );
				user[i].showLastChips();
			}else {
			}
		}
	}




	//導入・ルールの説明
	public static void introduct() {
		System.out.println(
				"【カジノのルール】" + "\n" +
				Play.gameKinds + "種類のゲームに最大で" + Play.gameTimes + "回チャレンジすることができます" + "\n" +
				"ただし、１種類のゲームにチャレンジできるのは" + Play.maxGameTimes + "回までです" + "\n\n" +
				"初めに" + (firstChip10*10 + firstChip1) + "ポイント分のチップが与えられ" + "\n" +
				"全てのチャレンジを終えた時点で保有しているチップのポイント数で競います" + "\n" +
				Play.gameTimes + "回のチャレンジの途中でチップが無くなった場合は、その時点でチャレンジ終了です" + "\n\n" +
				"↓各ゲームのルール↓\n\n\n");

		BigOrSmall.introduct();
		GuessCard.introduct();
		PairCards.introduct();

	}


	//ユーザー名を入力させる
	public static String inputUserName() {
		String userName = null;
		outside3 : while(true) {
			System.out.println("ユーザー名を入力して下さい");
			userName = new java.util.Scanner(System.in).nextLine();

			for(int i = 0; i<maxUser ; i++) {
				if(user[i] == null) {
				}else if(userName.equals( user[i].getUserName())) {
					System.out.println("登録済みのユーザー名です");
					System.out.print("もう一度別の");
					continue outside3;
				}else {
				}
			}
			return userName;
		}
	}


	/*チャレンジするBetGameを一つ選択
	 *選択したBetGameにチャレンジ
	 * チャレンジ後のチップをチェックし、次のゲームにチャレンジできるか判断する
	 * チャレンジ続行：可⇒true ／不可⇒false
	*/
	public static boolean playBetGame(User user[] , int i , int j) {
		int gameNum = Play.selectetBetGame(user,i);//user[i]がｊ番目にチャレンジするBetGameを選択する

		user[i].playBetGame(j,gameNum);//j番目のチャレンジでgameNum番のBetGameにチャレンジする
		betGameCnt[gameNum]++;//gameNum番のBetGameのチャレンジ回数をカウントする
		Game.setBonusRate(1);//bonusRateを１に戻す


		//j番目のチャレンジ終了時点でチップが0でないかチェックする
		if(user[i].checkChips(1)) {
			return true;
		}else {
			System.out.println( "チップが無くなったためチャレンジ終了です\n\n\n");
			return false;
		}
	}



	//BetGameの中からチャレンジするゲームを選択させる
	public static int selectetBetGame(User user[],int i) {

		int gameNum = 0; //選択したゲームの番号
		boolean []overGameTimes = new boolean [gameKinds]; //各BETゲームのチャレンジできる回数を超えていないか
		boolean []shortageChips = new boolean [gameKinds]; //各BETゲームにチャレンジするために必要なチップが足りているか

		while(true) {
			System.out.println("チャレンジするゲームを選んで下さい\n");

			for(int k = 0 ; k < gameKinds ; k++){
				overGameTimes[k] = true;
				shortageChips[k] = true;

				//チャレンジできる回数を超えていないかチェック
				if(betGameCnt[k] < maxGameTimes) {
					overGameTimes[k] = true;
				}else {
					overGameTimes[k] = false;
				}

				//チャレンジするために必要なチップが足りているかチェック
				if(user[i].checkChips(minBetPoint[k])){
					shortageChips[k] = true;
				}else {
					shortageChips[k] = false;
				}

				//条件を満たしている時
				if(overGameTimes[k] && shortageChips[k]) {
					System.out.println( "[" + betGameName[k] + "] ⇒ " + k );
				}
			}

			gameNum = Input.selectWhichOne(0,2);//0～2を入力して選択させる

			//チャレンジできる回数を超えている時
	        if(overGameTimes[gameNum] == false) {
				System.out.println( "入力エラーです");
				System.out.println( betGameName[gameNum] + "は既に" + maxGameTimes + "回チャレンジしています");
				System.out.print( "他の");
				continue;

			//チャレンジするために必要なチップが足りない時
	        }else if(shortageChips[gameNum] == false) {
				System.out.println( "入力エラーです");
				System.out.println( betGameName[gameNum] + "にチャレンジするためにはチップが" + minBetPoint[gameNum] + "ポイントが必要です");
				System.out.println( "現在の保有チップは"+ user[i].getChips().toString() + "です");
				System.out.print("他の");
				continue;

			//条件を満たしている時
	        }else {
				System.out.println( betGameName[gameNum] + "にチャレンジします\n" );
				break;
	        }

		}

		return gameNum;
	}


	//BetGameのチャレンジを続けるか選択させる
	public static boolean playNextBetGame(User user[],int i) {
		System.out.println("現在の保有チップは"+user[i].getChipsPoint()+"ポイントです");

		System.out.println("チャレンジを続けますか？");
		System.out.println("[Yes]⇒0");
		System.out.println("[No]⇒1");

		if(Input.selectEither()) {//0または１を入力して選択させる（0ならtrue，1ならfalse）
			System.out.println("Let's challenge next game!\n");
			return true;

		}else {
			System.out.println("チャレンジを終了します");
			return false;
		}


	}




}
