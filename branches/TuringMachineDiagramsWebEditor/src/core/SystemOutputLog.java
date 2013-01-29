package core;


public class SystemOutputLog extends Log {
	public SystemOutputLog(){
	}
	@Override
	public void Write(String text) {
		System.out.print(text);
	}

	@Override
	public void WriteLn(String text) {
		System.out.println(text);
	}

	@Override
	public void Clear() {
		//Do nothing
	}

}
