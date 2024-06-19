package mypackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class ToDoApp{
	  public static void main(String[] args) throws Exception{
	    Scanner sc = new Scanner(System.in);
	    File file=new File("todo.csv");
	    ArrayList<ToDo> list;
	    if(file.exists()){
	      list = loadFile(file);
	    }else{
	      list = new ArrayList<>();
	    }
	    if(list.size() == 0){
	      System.out.println("ToDoは1件もありません");
	    }else{
	      displayList(list);
	    }
	    while(true){
	      System.out.println("——操作を入力してください。——");
	      System.out.print("1/登録 2/重要度変更 3/削除 4/終了>");
	      int select = sc.nextInt();
	      switch(select){
	        case 1:
	          addItem(list,sc);
	          break;
	        case 2:
	          updateItem(list,sc);
	          break;
	        case 3:
	          deleteItem(list,sc);
	          break;
	        default:
	          System.out.println("アプリケーションを終了します。");
	          saveFile(file,list);
	          return;
	      }
	      sortList(list);
	      displayList(list);
	    }
	  }
	  
	  static void sortList(ArrayList<ToDo> list){
		    for(int i=0;i<list.size()-1;i++){
		      for(int j=i+1;j<list.size();j++){
		        if(list.get(i).importance < list.get(j).importance){
		          ToDo temp = list.get(i);
		          list.set(i,list.get(j));
		          list.set(j,temp);
		        }
		      }
		    }
		  }

	static void addItem(ArrayList<ToDo> list, Scanner sc){
		  System.out.println("新規Todoを作成します");
		  System.out.print("Todo内容を入力してください>");
		  String title = sc.next();
		  System.out.print("重要度を1～10(最大)で入力してください>");
		  int importance = sc.nextInt();
		  System.out.println("1件追加しました。");
		  ToDo t = new ToDo(title, importance);
		  list.add(t);
	}
	
	static void displayList(ArrayList<ToDo> list) {
		for(int i = 0; i < list.size();i++) {
			System.out.println(list.get(i).showStatus());
		}
		
	}
	
	static void updateItem(ArrayList<ToDo> list, Scanner sc) {
		if(list.size() == 0) {
			System.out.println("まだToDoがありません。");
			return;
		}
		System.out.printf("重要度を変更します。番号を入力してください。0～%d>",list.size()-1);
		int no = sc.nextInt();
		ToDo t = list.get(no);
		System.out.print("重要度を再設定してください>");
		int importance = sc.nextInt();
		t.changeImportance(importance);
		
	}
	
	static void deleteItem(ArrayList<ToDo> list, Scanner sc) {
		if(list.size() == 0) {
			System.out.println("まだToDoがありません。");
			return;
		}
		System.out.printf("ToDoを削除します。番号を入力してください。0～%d>",list.size()-1);
		int no = sc.nextInt();
		list.remove(no);
		System.out.println("1件削除しました。");
	}
	
	static ArrayList<ToDo> loadFile(File file) throws Exception{
		//リターンするリスト作成
		ArrayList<ToDo> list = new ArrayList<>();
		//読み込みはfis
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		
		String line;
		while((line = br.readLine()) != null) {
			String[] values=line.split(",");
			//タイトルの取り出し
			String title = values[0];
			//重要度の取り出し
			int importance = Integer.parseInt(values[1]);
			//2つの情報をもとにToDoインスタンスを作成
			ToDo t = new ToDo(title, importance);
			//リストに加える
			list.add(t);
		}
		//br終了処理
		br.close();
		//作成されたリストを返す
		return list;
	}
	
	static void saveFile(File file,ArrayList<ToDo> list) throws Exception{
		//書き込みはfos
		FileOutputStream fos = new FileOutputStream(file);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
	    BufferedWriter bw = new BufferedWriter(osw);
	    
	    //受け取ったlistを拡張for出まわす
	    for(ToDo c: list) {
	    	//インスタンスの情報をcsvで書き込む
	    	bw.write(c.toCSV());
	    	//改行
	    	bw.newLine();
	    }
	    //ファイルを閉じる
	    bw.close();
	}

}


class ToDo{
	//フィールド
	String title;
	int importance;
	
	//コンストラクタ
	ToDo(String title, int importance){
		this.title = title;
		this.importance = importance;
	}
	//インスタンスメソッド
	public String showStatus() {
		return String.format("%s/重要度:%d",this.title,this.importance);
	}
	
	void changeImportance(int importance) {
		this.importance = importance;
		System.out.println("重要度を変更しました。");
	}
	
	String toCSV(){
	    return String.format("%s,%d",this.title,this.importance);
	  }
	
}
