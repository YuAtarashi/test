package url;

public class Card {


	/*
	【BigOrSmallでのカードの強さ】
	・数字が大きい方が強い
	・同じ数字の場合は「スペード」 > 「ハート」 > 「ダイヤ」 > 「クラブ」
	  ⇒カードのIDはカードの強さと一致させ、下記の通りに設定します
		id = 0 : クラブ１ (0÷4=0あまり0 ⇒ num=0/4+1=1 markNum=0%4=0)
		id = 1 : ダイヤ1  (1÷4=0あまり1 ⇒ num=0/4+1=1 markNum=0%4=1)
		id = 2 : ハート1  (2÷4=0あまり2 ⇒ num=0/4+1=1 markNum=0%4=2)
		id = 3 : スペード1(3÷4=0あまり3 ⇒ num=0/4+1=1 markNum=0%4=3)
		id = 4 : クラブ2  (4÷4=1あまり0 ⇒ num=0/4+1=2 markNum=0%4=0)
		id = 5 : ダイヤ2  (5÷4=1あまり1 ⇒ num=0/4+1=2 markNum=0%4=1)
		id = 6 : ハート2  (6÷4=1あまり2 ⇒ num=0/4+1=2 markNum=0%4=2)
		id = 7 : スペード2(7÷4=1あまり3 ⇒ num=0/4+1=2 markNum=0%4=3)

		id = markNum + (num-1)*4

	*/
	private int id;//カードの固有番号（通常カードは0～51、ジョーカーは52）
	private int num ;//カードの番号（1～13）
	private int markNum ;//数字で表したカードのマーク
	private String mark;//markNumからに変換したマーク名

	//コンストラクタ（引数１）
	public Card(int id) {
		if(id<0 || id>52) {
			System.out.println("引数idは0～52の整数のみです");
		}else {
			this.id = id;

			if(this.id == 52) {
				this.num = 0;
				this.markNum = 4;
			}else {
				this.num = id/4 +1 ;
				this.markNum = id%4 ;
			}
			this.conversionMark();
		}
	}

	//コンストラクタ（引数２）
	//id = markNum + (num-1)*4
	public Card(int num, int markNum) {
		if(num<1 || num>13) {
			System.out.println("第一引数numは1～13の整数のみです");
		}else if(markNum<0 || markNum>4) {
			System.out.println("第二引数markNumは0～4の整数のみです");
		}else if(markNum == 4) {
			this.num = 0 ;
			this.markNum = markNum;
			this.id = 52;
			this.mark = "ジョーカー";
		}else {
			this.num = num ;
			this.markNum = markNum;
			this.id = markNum + (num-1)*4;
			this.conversionMark();
		}
	}


	//markNumからマーク名に変換
	public void conversionMark() {
		switch(this.markNum) {
		case 0:
			this.mark = "クラブ";
			break;
		case 1:
			this.mark = "ダイヤ";
			break;
		case 2:
			this.mark = "ハート";
			break;
		case 3:
			this.mark = "スペード";
			break;
		case 4:
			this.mark = "ジョーカー";
			break;
		}
	}



	//toStringのオーバーライド
	@Override
	public String toString() {
		if(this.id == 52) {
			return "〔" + this.mark + "〕";
		}else {
			return  "〔" + this.mark + "／"+this.num + "〕" ;
		}
	}


	//equalsのオーバーライド（idが同じなら同インスタンス）
	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}else if(o instanceof Card) {
			Card c = (Card) o;
			if(this.id == c.id) {
				return true;
			}
		}
		return false;
	}


	//numが同じでtrue
	public boolean equalsNum(Card card) {
		if(this.num == card.num ) {
			return true;
		}
		else {
			return false;
		}
	}

	//markNumが同じでtrue
	public boolean equalsMark(int markNum) {
		if(this.markNum == markNum ) {
			return true;
		}
		else {
			return false;
		}
	}


	//２つのカードインスタンスのidの大・小を比較
	//引数のCardインスタンスのidの方が大きければtrue
	public boolean compareID(Card card) {
		if(this.id < card.id) {
			return true;
		}
		else {
			return false;
		}
	}

	//IDのゲッターメソッド

	public int getID() {
		return this.id;
	}

	//カード番号のゲッターメソッド
	public int getNum() {
		return this.num;
	}

	//マークナンバーのゲッターメソッド
	public int getMarkNum() {
		return this.markNum;

	}

}
