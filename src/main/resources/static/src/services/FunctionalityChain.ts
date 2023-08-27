import FileService from './FileService';
import AuthService from './AuthService';

export interface UserResponse {
    userId:string,
    name:string,
    email:string,
    details:string,
    pic:string,
    phone:string,
    age:string,
    gender:string
}

export interface ServiceResponse {
    serviceId:string,
    name:string,
    pic:string,
    email:string,
    details:string
}

export let FUNCTIONALILTY_COMMANDS = {
    UPLOAD : "upload",
    USER_SIGN_UP : "user_sign_up",
    USER_LOG_IN : "user_log_in",
    SERVICE_SIGN_UP : "service_sign_up",
    SERVICE_SIGN_IN : "service_sign_in",
    GIVE_SERVICE_ACCESS_TO_USER : "give_service_access_to_user"
}

let map = new Map<string,Function>(); 
let init = ()=>{
    map.set(FUNCTIONALILTY_COMMANDS.UPLOAD,FileService.upload);
    map.set(FUNCTIONALILTY_COMMANDS.GIVE_SERVICE_ACCESS_TO_USER,AuthService.giveServiceAcessToUser);
    map.set(FUNCTIONALILTY_COMMANDS.USER_LOG_IN,AuthService.userLogIn)
    map.set(FUNCTIONALILTY_COMMANDS.USER_SIGN_UP,AuthService.userSignUp);
    map.set(FUNCTIONALILTY_COMMANDS.SERVICE_SIGN_IN,AuthService.serviceLogIn);
    map.set(FUNCTIONALILTY_COMMANDS.SERVICE_SIGN_UP,AuthService.serviceSignUp);
}

let defaultFunc:Function = (body:any,prevData:any)=>{
    return {success:false};
};

let handle = (command : string) : Function => {
    let cb = map.get(command);
    if(cb == undefined) return defaultFunc;
    return cb!;
}

init();

export default {
    handle
}