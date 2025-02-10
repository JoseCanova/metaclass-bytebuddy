package org.nanotek.metaclass.bytebuddy;

import java.util.Optional;

public class Holder<S> {
	S s;
	
	public static <S> Holder<S> of(S s){
		return new Holder<S>().put(s);
	}
	
	public Holder<S> put(S s){
		this.s = s;
		return  this;
	}
	public  Optional<S> get(){
		return Optional.ofNullable(s);
	}
	
	@Override
	public String toString() {
		return "Holder [s=" + s + "]";
	}
	
	
}