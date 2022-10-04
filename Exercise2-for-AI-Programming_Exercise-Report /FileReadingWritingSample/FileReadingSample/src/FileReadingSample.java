import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

class FileReadingSample {

    public static void main(String arg[]){
        try {    // ファイル読み込みに失敗した時の例外処理のためのtry-catch構文
        	//eclipseの場合プロジェクトフォルダがカレントディレクトリになるため,
        	//"hoge.txt"をsrc以下においた場合のパスは以下のようになる
            String fileName = "src/hoge.txt"; // 相対パスを使ってファイル名指定

            // 文字コードUTF-8を指定してBufferedReaderオブジェクトを作る
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));

            // 変数lineに1行ずつ読み込むfor文
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                System.out.println(line);  // 1行表示
            }
            in.close();  // ファイルを閉じる
        } catch (IOException e) {
            e.printStackTrace(); // 例外が発生した所までのスタックトレースを表示
        }
    }
}