package main;

import org.jnativehook.keyboard.NativeKeyEvent;

import java.awt.*;
import java.io.*;

/**
 * Created by Knight on 15/10/1.
 */
public class TrySpeech extends Frame {

	boolean command = false, c = false, altRight = false,backSpace=false;

	boolean firstCtrl = true;  //判断是不是第一次复制 为了处理后面可能的第二次 command+c的复制情况
	boolean altPauseFuncion = true;

	boolean stopAllFunction=false;
	int nowPid = -1;

	//String shellScriptpath="/Users/Knight/Documents/workspace/shellScripts/";
	String shellScriptpath="/Users/Knight/Documents/workspace/TextToSpeech/shellScripts/";
	// static BufferedWriter bufferedWriter=null;
	public  void startIt() {

		this.launch();

	}

	public void press(NativeKeyEvent e ) {
		int pressNum = e.getKeyCode();

		switch (pressNum) {
			case NativeKeyEvent.VC_META_L:  // 左边的command键盘
				System.out.println("\n-----pressed cmd");

				command = true;
				break;
			case NativeKeyEvent.VC_C:
				System.out.println("\n-------pressed c");

				c = true;
				break;
			case NativeKeyEvent.VC_ALT_R :
				System.out.println("\n-------pressed altRight");
				altRight = true;
				break;
			case NativeKeyEvent.VC_BACKSPACE:
				System.out.println("\n-------pressed backSpace");
				backSpace = true;
				break;
		}
		afterPress();

	}

	public  void release(NativeKeyEvent e) {
		int pressNum = e.getKeyCode();
		switch (pressNum) {
			case NativeKeyEvent.VC_META_L:
				System.out.println("\n------released cmd");

				command = false;
				break;
			case NativeKeyEvent.VC_C :

				System.out.println("\n------released c");

				c = false;
				break;
			case NativeKeyEvent.VC_ALT_R :
				System.out.println("\n------released altRight");

				altRight = false;
				break;
			case NativeKeyEvent.VC_BACKSPACE:
				System.out.println("\n------released backSpace");
				backSpace=false;
				break;
		}
		afterPress();
	}
	public void afterPress() {
		if(command && backSpace){
			stopAllFunction=!stopAllFunction;
			if(stopAllFunction==true){
				stopOrContRead(nowPid,1);

				System.out.println("\n------ stop all function now ,if you want to start again,please press command+p again! ");
			}
			else {

				stopOrContRead(nowPid,2);
				System.out.println("\n------ ^^ start all function now ,if you want to stop,please press command+p! ");
			}
		}

		if(stopAllFunction){

			return;
		}

		if (command && c) {
			//System.out.println("start insert");

			if(stopAllFunction){
				return;
			}


			if (firstCtrl) {
				insertClipToFile();
				System.out.println("\n------start reading for the first time");
				firstCtrl=false;
				nowPid = startRead();
			} else {
				System.out.println("\n------stop reading the paragraph now,starting next.");
				altPauseFuncion =true;
				stopOrContRead(nowPid, 0);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				insertClipToFile();
				System.out.println("\n------start reading again,previous paragraph ignored");

				nowPid = startRead();
			}
		}

		if (altRight) {
			if (nowPid == -1)
				return;
			System.out.println("\n-----stop reading  since pressing altRight(first time)");

			if (altPauseFuncion) {
				System.out.println("\n------ pause it , type 1 hang the process ");

				stopOrContRead(nowPid, 1);
				altPauseFuncion = !altPauseFuncion; //
			} else {
				System.out.println("\n----- recover it ,type 2 continue the process");

				stopOrContRead(nowPid, 2);
				altPauseFuncion = !altPauseFuncion;
			}
		}

	}



	private class paintThread implements Runnable {
		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


	public void insertClipToFile() {
		try {

			String[] cmd = new String[]{"sh", "-c", "pbpaste "};
			Process process = Runtime.getRuntime().exec(cmd);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			File file = new File(
					shellScriptpath+"content.txt");
			StringBuilder total = new StringBuilder();
			String str = null;
			BufferedWriter bufferedWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
			while ((str = bufferedReader.readLine()) != null) {

				String n_str=new String(str.getBytes(),"UTF-8");
				String n_str1=new String(str.getBytes(),"ISO-8859-1");
				String n_str2=new String(str.getBytes(),"GB2312");
				String n_str3=new String(str.getBytes(),"US-ASCII");
				String n_str4=new String(str.getBytes(),"GBK");

				String str_cp= str;
				int start=0;
				String str_write;
				String [] toWriteArr= str_cp.split(",|\\.|，|。");
				for(int i=0;i<toWriteArr.length;i++){
					str_write=toWriteArr[i];
					str_write+=",";  // 为了停顿一下

					bufferedWriter.write(str_write);
					bufferedWriter.write("\n");

					total.append(str_write);
					total.append("\n");// 另一行
					System.out.println(str); // 这个地方可以不写

				}

				/*while(str_cp.length()>5){

					str_write=str.substring(start,start+5);
					bufferedWriter.write(str_write);
					bufferedWriter.write("\n");

					total.append(str_write);
					total.append("\n");// 另一行
					System.out.println(str);

					str_cp=str.substring(start+5);
					start+=5;
				}
				if(str_cp.length()>0){
					bufferedWriter.write(str_cp);
					bufferedWriter.write("\n");

					total.append(str_cp);
					total.append("\n");// 另一行
					System.out.println(str);
				}*/
				/*total.append(str);
				total.append("\n");// 另一行
				bufferedWriter.write(str);
				bufferedWriter.write("\n");
				System.out.println(str);*/
			}
			System.out.println("total is " + total);
			//String toWrite = total.toString();
			//buffe redWriter.write(toWrite);

			bufferedReader.close();
			bufferedWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void launch() {

        /*GlobalKeyListenerDemon globalKeyListenerDemon = new GlobalKeyListenerDemon();
*/

		new Thread(new paintThread()).start();

/*

		// try {// String lalalalala= "wowowwowoow";
		// String lalalalala= "shenmeqingkuang";

		// Runtime.getRuntime().exec(new String[]{"ls ", fname});

		// String [] cmd =new String [] {"sh","-c", "pbpaste"};
		// String [] cmd =new String [] { "/usr/bin/pbpaste"};
		// String [] cmd =new String [] { "sh","-c","ls /"};

		/*
		 * Process process = Runtime.getRuntime().exec(cmd); BufferedReader
		 * bufferedReader =new Buf
		 *//*
			 * feredReader(new InputStreamReader(process.getInputStream()));
			 * 
			 * File file =new File
			 * ("/Users/Knight/Documents/workspace/shellScripts/content.txt");
			 */

		// OutputStreamWriter write = new OutputStreamWriter(new
		// FileOutputStream(file),"UTF-8");

		/*
		 * StringBuilder total =new StringBuilder(); String str =null;
		 * BufferedWriter bufferedWriter =new BufferedWriter(new
		 * OutputStreamWriter(new FileOutputStream(file),"UTF-8")); while( (
		 * str=bufferedReader.readLine())!=null){ total.append(str);
		 * System.out.println(str); //BufferedWriter bufferedWriter =new
		 * BufferedWriter(new OutputStreamWriter(new
		 * FileOutputStream(file),"UTF-8")); readIt(bufferedWriter,str);
		 * 
		 * 
		 * }
		 * 
		 * 
		 * 
		 * 
		 * bufferedReader.close(); //bufferedWriter.close(); String toRead =
		 * total.toString(); String toReada= new String(toRead.getBytes());
		 */

		/*
		 * process = Runtime.getRuntime().exec( new
		 * String[]{"sh","-c"," echo  "+toReada+
		 * " < /Users/Knight/Documents/workspace/shellScripts/content.txt"});
		 */

		/*
		 * process = Runtime.getRuntime().exec( new String[] {"sh","-c",
		 * " say <   /Users/Knight/Documents/workspace/shellScripts/content.txt"
		 * });
		 */

		/*
		 * bufferedReader =new BufferedReader(new
		 * InputStreamReader(process.getInputStream()));
		 */

		/*
		 * while( ( str=bufferedReader.readLine() )!=null){
		 * System.out.println("alialilai"+str); }
		 */
		/*
		 * Thread.sleep(1000); System.out.println("finish process "); } catch
		 * (IOException e) { e.printStackTrace(); } catch (InterruptedException
		 * e) { e.printStackTrace(); }
		 */

//*/
	}

	public int startRead() { // return pid num
		Process process = null;
		int pidNum = -1;
		try {
			process = Runtime
					.getRuntime()
					.exec(new String[]{"sh", "-c",
							" sh "+shellScriptpath+"speakFromContent.sh"});

			new Thread(new ReadOutPutInfo(process)).start();//一个多线程解决了问题

            Thread.sleep(2000);

			File file = new File(
					shellScriptpath+"nowpid");

			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(file)));

			StringBuilder sb = new StringBuilder();
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				sb.append(str);
				System.out.println("\n-------now the pid is -------------------->"+str);
			}
			pidNum = Integer.parseInt(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pidNum;
	}

	public void stopOrContRead(int pid_now, int type) {
		Process process = null;
        String cmd= null;
        if (type ==0){    // kill 掉 当前的pid 进程
            cmd = "sh "+shellScriptpath+"killSpeak.sh";
        }
        else if (type ==1){  // paulse 暂停  两下 beep 声音
			for(int i=0;i<2;i++) {
				java.awt.Toolkit.getDefaultToolkit().beep();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
            cmd = "sh "+shellScriptpath+"stopSpeak.sh";
        }
        else if (type ==2){  // 恢复朗读 一下 beep 声音
			for(int i=0;i<1;i++) {
				java.awt.Toolkit.getDefaultToolkit().beep();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
            cmd ="sh "+shellScriptpath+"continueSpeak.sh";
        }
		try {
			process = Runtime.getRuntime().exec(
					new String[]{
							"sh",
							"-c",
							cmd});

			File file = new File(
					shellScriptpath+"nowpid");

			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			String str = null;

			while ((str = bufferedReader.readLine()) != null) {
				System.out.println("res of exec -->" + str);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readIt(BufferedWriter bufferedWriter, String str) {
		try {
			bufferedWriter.write(str);
			bufferedWriter.close();
            Process process = Runtime
                    .getRuntime()
                    .exec(new String[]{"sh", "-c",
                            " say <   "+shellScriptpath+"content.txt"});
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	private class ReadOutPutInfo implements Runnable{

		Process readProcess = null;
		public ReadOutPutInfo( Process process){
			this.readProcess= process;

		}
		public void run() {


			InputStreamReader ir= new InputStreamReader(readProcess.getInputStream());
			LineNumberReader input=new LineNumberReader(ir);
			String line;
			try {
				while ((line = input.readLine())!=null){
                    System.out.print(line);
                }
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
