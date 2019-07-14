package url;

public class Chips {

	private int chip10;//１０ポイントチップの保有枚数
	private int chip1;//１ポイントチップの保有枚数
	private int point;	//保有チップのポイント

	//コンストラクタ（ゲーム開始の初期値にセット）
	public Chips() {
		this.chip10= Play.firstChip10;
		this.chip1 = Play.firstChip1;
		conversionPoint() ;
	}

	//10ポイントチップと1ポイントチップの枚数からポイントを算出
	public void conversionPoint() {
		this.point = this.chip10*10 + this.chip1 ;
	}


	//チップを没収(BET）する
	public void betChips(int point) {
		if(chip10 >= point/10) {//没収するチップの十の位が保有する10ポイントチップの枚数以下の時
			this.chip10 -= point/10;//10ポイントチップの枚数を没収するチップの十の位の数だけ減らす
			this.chip1 -= point%10;//1ポイントチップの枚数を没収するチップの一の位の数だけ減らす
		}else {//没収するチップの十の位が保有する10ポイントチップの枚数を超える時
			point -= this.chip10*10;//保有する10ポイントチップはすべて支払う
			this.chip10 = 0;
			this.chip1 -= point;//残りは1ポイントチップで支払う
		}
		conversionPoint() ;
	}


	//チップを与える
	public void winChips(int point) {
		this.chip10 += point/10;
		this.chip1 += point%10;
		conversionPoint() ;
	}

	//toStringのオーバーライド
	@Override
	public String toString() {
		conversionPoint() ;
		return   this.point  +"ポイント(10ポイントチップ:"+ this.chip10 +"枚／1ポイントチップ:"+ this.chip1 +"枚)";
	}

	//10ポイントチップの枚数のゲッターメソッド
	public int getChip10() {
		return this.chip10;
	}

	//1ポイントチップの枚数のゲッターメソッド
	public int getChip1() {
		return this.chip1;
	}

	//チップポイントのゲッターメソッド
	public int getPoint() {
		return this.point;
	}











}
