package si.urbas.pless.util;

public interface Function<TParameter, TReturn> {
  TReturn invoke(TParameter parameter);
}
