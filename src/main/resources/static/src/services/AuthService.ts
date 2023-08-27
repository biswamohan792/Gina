import URLs from "../constants/URLs";
import { ServiceResponse, UserResponse } from "./FunctionalityChain";

export interface ServiceSignUpBody {
  name: string;
  pic: string;
  password: string;
  email: string;
  details: string;
}

export interface UserSignUpBody extends ServiceSignUpBody {
  age: string;
  phone: string;
  gender: string;
}

export interface InputType extends UserSignUpBody{
  file:File | null
}

let logIn = async (
  type: "user" | "service",
  body: {
    email: string;
    password: string;
  }
) => {
  try{
    let res = (await (await fetch(URLs.DOMAIN+URLs.LOG_IN.path,{
      method:URLs.LOG_IN.method,
      headers:{"Content-Type":"application/json"},
      body:JSON.stringify({...body,type}),
      credentials:'include'
    })).json());
    return res;
  }catch(e:any){
    return {
      success:false,
      error:"Login Failed, Sorry man😐!"
    }
  }
};

let signUp = async (
  type: "user" | "service",
  body: UserSignUpBody | ServiceSignUpBody
) => {
  try{
    let res = (await (await fetch(URLs.DOMAIN+URLs.SIGN_UP.path,{
      method:URLs.SIGN_UP.method,
      headers:{"Content-Type":"application/json"},
      body:JSON.stringify({...body,type})
    })).json());
    return res;
  }catch(e:any){
    return {
      success:false,
      error:"SignUp Failed, Sorry man😐!"
    }
  }
};

let userLogIn = (body:any,prevData:any) => {
  return logIn("user", {
    email:body["email"],
    password:body["password"]
  });
};

let serviceLogIn = (body:any,prevData:any) => {
  return logIn("service", {
    email:body["email"],
    password:body["password"]
  });
};

let userSignUp = (body:any,prevData:any) => {
  return signUp("user", {
    age:body["age"],
    details:body["details"],
    email:body["email"],
    gender:body["gender"],
    name:body["name"],
    password:body["password"],
    phone:body["phone"],
    pic:body["pic"]
  });
};

let serviceSignUp = (body:any,prevData:any) => {
  return signUp("service", {
    details:body["details"],
    email:body["email"],
    name:body["name"],
    password:body["password"],
    pic:body["pic"]
  });
};

let giveServiceAcessToUser = async (body:any,prevData:any) => {
  let userId = prevData["userId"];
  let serviceId = prevData["serviceId"];
  if(userId==null || userId==undefined || serviceId==null || serviceId==undefined){
    return {
      success:false,
      error:"API Error: ServiceId/UserId not present!"
    }
  }
  try{
    let res = (await(await fetch(URLs.DOMAIN+URLs.PERMISSION_REQUEST.path,{
      method:URLs.PERMISSION_REQUEST.method,
      headers:{"Content-Type":"application/json"},
      body:JSON.stringify({userId,serviceId})
    })).json());
    return res;
  }catch(e:any){
    return {
      success:false,
      error:"API Error: Permission Issue!"
    }
  }
}

export default {
  userLogIn,
  serviceLogIn,
  userSignUp,
  serviceSignUp,
  giveServiceAcessToUser
};
