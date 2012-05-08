package gwt.client.def;

import gwt.shared.model.SRule.Comm;

public abstract class FFFDef<T> {
	
	public abstract String getRss(T t);
	public abstract int getRListSize(T t);
	public abstract String getRKeyString(T t, int i);
	public abstract Comm getRComm(T t, int i);
	public abstract String getRArg(T t, int i);
}
