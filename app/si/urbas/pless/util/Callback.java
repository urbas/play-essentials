package si.urbas.pless.util;

public interface Callback<TParameter> {
  void invoke(TParameter parameter);
}
