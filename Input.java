package url;

import java.util.InputMismatchException;

public class Input {

	//入力して選択させる（ 0⇒true / 1⇒false )
		static boolean selectEither() {

			while(true) {
				try {
					int input =  new java.util.Scanner(System.in).nextInt();

					if(input==0) {
						return true;
					}else if(input==1) {
						return false;
					}
					else{
						throw new InputMismatchException();
					}

				}catch(InputMismatchException  e){
					System.out.println( "入力エラーです \n " );
					System.out.println("半角数字の0あるいは1のみ入力してください");
				}
			}
		}

	//複数の中から一つを選択させる
		static int selectWhichOne(int mini ,int max) {
			while(true) {
				try {
					int input =  new java.util.Scanner(System.in).nextInt();

					if(input >= mini && input <= max) {
						return input;
					}else{
						throw new InputMismatchException();
					}

				}catch(InputMismatchException  e){
					System.out.println( "入力エラーです \n " );
					System.out.println( mini + "～"+ max +"までの半角数字入力してください");
				}
			}
		}

}
