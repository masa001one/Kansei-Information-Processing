import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

class FileWritingSample {

    public static void main(String arg[]){
        try {    // ファイル読み込みに失敗した時の例外処理のためのtry-catch構文
        	//eclipseの場合プロジェクトフォルダがカレントディレクトリになるため,
        	//"hoge.txt"をsrc以下においた場合のパスは以下のようになる
            String fileName = "src/hogehoge.txt"; // 相対パスを使ってファイル名指定

            // 文字コードUTF-8を指定してPrintWriterオブジェクトを作る。
            PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));

            out.println("Yoko is a funny girl");    // 1行書き込み
            out.println("Kei is a cute infant boy");    // 1行書き込み
            out.close();        // ファイルを閉じる
        } catch (IOException e) {
            e.printStackTrace(); // 例外が発生した所までのスタックトレースを表示
        }
    }
}
