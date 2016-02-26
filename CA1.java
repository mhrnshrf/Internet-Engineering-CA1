import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;



public class CA1 {

	public static void main(String[] args) throws IOException {
		HelpServer hp = new HelpServer(args[1], Integer.parseInt(args[2]));
		ServerSocket s = new ServerSocket(Integer.parseInt(args[0]));
		//ServerSocket s = new ServerSocket(7071);
		try {
			Socket socket = s.accept();
			try {
				System.out.println("Connection accepted: "+ socket);
				BufferedReader in = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream())),true);
				while (true) {
					StringBuilder name = new StringBuilder(), price = new StringBuilder(), stock = new StringBuilder();
					int priceNum, stockNum;
					String line = in.readLine();
					
					if(line.startsWith("BUY ")){

						String subline = line.substring(4);
						if(parseMessage(name, price, stock, subline) == -1){
							out.println("DECLINED");
							out.flush();
							continue;
						}
						stockNum = Integer.parseInt(stock.toString());
						priceNum = Integer.parseInt(price.toString());
						
						StringBuilder req = new StringBuilder();
						req.append("GET ");
						req.append(name);
						
						String answer = hp.RequestHandler(req.toString());
						if(answer.startsWith("Unknown symbol")){
							out.println("DECLINED");
							out.flush();
							continue;
						}
						int realPrice = findPrice(answer);
						
						if(priceNum - realPrice > realPrice/10 || realPrice - priceNum > realPrice/10){
							out.println("DECLINED");
							out.flush();
						}
						else{
							out.println("APPROVED");
							out.flush();
						}
							
						
					}
					else if(line.startsWith("SELL ")){
						String subline = line.substring(5);
						if(parseMessage(name, price, stock, subline) == -1){
							out.println("DECLINED");
							out.flush();
							continue;
						}
						
						stockNum = Integer.parseInt(stock.toString());
						priceNum = Integer.parseInt(price.toString());
						
						StringBuilder req = new StringBuilder();
						req.append("GET ");
						req.append(name);
						
						String answer = hp.RequestHandler(req.toString());
						if(answer.startsWith("Unknown symbol")){
							out.println("DECLINED");
							continue;
						}
						int realPrice = findPrice(answer);
						
						if(priceNum - realPrice > realPrice/10 || realPrice - priceNum > realPrice/10)
							out.println("DECLINED");
						else
							out.println("APPROVED");
							
						
					}
					else if(line.equals("GET ALL")){
						StringBuilder result = new StringBuilder();
						String answer = hp.RequestHandler("GET ALL");
						//getAllStocks("Rena Investment(RENA1)$1401;Nirou Moharreke(NMOH1)$2211;B.A Oil Refinie(PNBA1)$3635;Saipa Inv.(SSAP1)$1450;Iran Khodro D.(KADR1)$1080;Zamyad(ZMYD1)$2004;Shahed Inv.(SAHD1)$1402;Behran Oil(NBEH1)$8852;Parsian Ecommerc(EPRS1)$3289;", result);
						getAllStocks(answer, result);
						out.println(result);
					}
					else{
						out.println("DECLINED");
						out.flush();
					}
					
				}
			} finally {
				System.out.println("closing...");
				socket.close();
			}
		} finally {
			s.close();
		}

		//System.out.println(hp.RequestHandler("GET ALL"));
	}

	private static int findPrice(String answer) {
		int i;
		for(i=0; i<answer.length() && answer.charAt(i) != '$'; i++);
		int realPrice = Integer.parseInt(answer.substring(i+1));
		return realPrice;
	}

	private static int parseMessage(StringBuilder name, StringBuilder price,
			StringBuilder stock, String subline) {
		
		int i;
		for(i = 0; i<subline.length() && subline.charAt(i) != ' '; i++)
			name.append(subline.charAt(i));
		
//		System.err.println(name.length());
		
		if(name.equals("") || name.length() == 0){
			return -1;
		}
			
		for(i = i+1; i<subline.length() && subline.charAt(i) != ' '; i++)
			stock.append(subline.charAt(i));
//		System.out.println(stock);
		if(stock.equals("") || stock.length() == 0){
			return -1;
		}
		
		
		for(i = i+1; i<subline.length() && subline.charAt(i) != ' ' && subline.charAt(i) != '\n'; i++)
			price.append(subline.charAt(i));
		
		if(price.equals("") || price.length() == 0){
			return -1;
		}
//		System.out.println(name);
		return 0;
	}
	
	private static void getAllStocks(String answer, StringBuilder result){
		int i, begin, end;
		String name, price;
		String [] lines = answer.split(";");
		
		for (String subline : lines){
			
			for(i=0; i<subline.length() && subline.charAt(i) != '('; i++);
			begin = i+1;
			for(i=i+1; i<subline.length() && subline.charAt(i) != ')'; i++);
			end = i;
			name = subline.substring(begin,end);
			for(i=i+1; i<subline.length() && subline.charAt(i) != '$'; i++);
			price = subline.substring(i+1);
			
			result.append("(");
			result.append(name);
			result.append(", ");
			result.append(price);
			result.append(")");
		}	
		
	}
}

