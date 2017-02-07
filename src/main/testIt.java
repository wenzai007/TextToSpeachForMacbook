package main;


/**
 * Created by Knight on 15/10/5.
 */
public class testIt {

	public static void main(String[]args){
		String a="abcdef";
		String a1= a.substring(0,5);
		String a2= a.substring(5);
		String a3=a;
		/*AudioClip audio;
		audio=getAudioClip(getCodeBase(),"Hi.au");
		audio.play();*/

		for(int i=0;i<2;i++) {
			java.awt.Toolkit.getDefaultToolkit().beep();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String str_cp = "asdfs,asdf,asdf.sad.eeee.eea，撒旦覅额啊，阿斯顿发，阿斯达。asfd。撒地方，阿斯顿f。";
		String [] toWriteArr= str_cp.split(",|\\.|，|。");
		String aa="ehe";

	}
}
