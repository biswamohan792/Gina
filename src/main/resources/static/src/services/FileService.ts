import express from 'express';
import URLs from '../constants/URLs';

let upload = async (body:any,prevData:any) => {
    if(prevData != undefined && prevData !=null){
        if(prevData["success"]==false)
            return prevData;
    }
    try{
        let formData = new FormData();
        formData.append("file",body["file"]);
        let res =  (await (await fetch(URLs.DOMAIN+URLs.FILE_UPLOAD.path,{
            method:URLs.FILE_UPLOAD.method,
            body:formData
        })).json());
        body["pic"]=res["body"];
        return res;
    }catch(e:any){
        return {
            success:false,
            error:"Failed to upload User Pic 📸!"
        };
    }
}

export default {
    upload
}