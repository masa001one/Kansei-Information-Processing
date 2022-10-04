import java.util.*;
import java.io.*;

class FileManager {
	FileReader f;
	StreamTokenizer st;

	public ArrayList<Rule> loadRules(String theFileName) { // 引数で与えられたファイル名に対してrules(ArrayList)を返却する
		ArrayList<Rule> rules = new ArrayList<Rule>();
		String line;
		try {
			int token;
			f = new FileReader(theFileName);
			st = new StreamTokenizer(f);
			while ((token = st.nextToken()) != StreamTokenizer.TT_EOF) { // nextToken() : このトークナイザの入力ストリームの次のトークンを構文解析する
				switch (token) {
					case StreamTokenizer.TT_WORD: // TT_WORD : ワードトークンが読み込まれたことを示す定数
						String name = null;
						ArrayList<String> antecedents = null;
						String consequent = null;
						if (st.sval.equals("rule")) { // sval 現在のトークンがワードトークンの場合、このフィールドにはワードトークンの文字を表す文字列が入る
							st.nextToken();
							name = st.sval;
							st.nextToken();
							if (st.sval.equals("if")) {
								antecedents = new ArrayList<String>();
								st.nextToken();
								while (!st.sval.equals("then")) {
									antecedents.add(st.sval);
									st.nextToken();
								}
								st.nextToken();
								consequent = st.sval;
							}
						}
						rules.add(new Rule(name, antecedents, consequent)); // ルール名, 前件, 後件を引数としたルールを一つ加える
						break;
					default:
						System.out.println(token);
						break;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return rules;
	}

	public ArrayList<String> loadWm(String theFileName) { // 引数で与えられたファイル名に対してwm(StringのArrayList)を返却する
		ArrayList<String> wm = new ArrayList<String>();
		String line;
		try {
			int token;
			f = new FileReader(theFileName);
			st = new StreamTokenizer(f);
			st.eolIsSignificant(true); // 行の終わりをトークンとして処理するかどうかを判別する(行末文字が独立したトークンとして処理)
			st.wordChars('\'', '\''); // 範囲の下限\' 上限\'
			while ((token = st.nextToken()) != StreamTokenizer.TT_EOF) { // nextToken() : このトークナイザの入力ストリームの次のトークンを構文解析する
				line = new String();
				while (token != StreamTokenizer.TT_EOL) { // TT_EOL : 行の終わりが読み込まれたことを示す定数
					line = line + st.sval + " "; // 現在のトークンがワード・トークンの場合、このフィールドには、ワード・トークンの文字を表す文字列が入る
					token = st.nextToken();
				}
				wm.add(line.trim()); // String.trim() : 文字列の先頭と末尾に空白があった場合はそれを取り除いて文字列を返却するメソッド
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return wm;
	}
}
