package com.pless;

import play.mvc.Controller;
import play.mvc.Result;

public class WelcomeController extends Controller {
  
  public static Result welcome() {
    return ok("Welcome to Pless!");
  }

}
