import React,{ createRef, useContext, useState } from 'react';
import './AuthUserService.scss';
import Bg from '../pics/bg.jpg';
import {context as NotificationContext} from '../context/NotificationContext';
import {context as ServiceDetailsContext} from '../context/ServiceDetailsContext';
import { useNavigate } from 'react-router-dom';
import { InputType } from '../services/AuthService';
import FunctionalityChain, { FUNCTIONALILTY_COMMANDS } from '../services/FunctionalityChain';

const Service = () => {
    let [signIn,setSignIn] = useState(true);

    let emailSignInRef = createRef<HTMLInputElement>();
    let passwordSignInRef = createRef<HTMLInputElement>();

    let emailSignUpRef = createRef<HTMLInputElement>();
    let passwordSignUpRef = createRef<HTMLInputElement>();
    let nameSignUpRef = createRef<HTMLInputElement>();
    let fileSignUpRef = createRef<HTMLInputElement>();

    let notificationContext = useContext(NotificationContext);
    let serviceDetailsContext = useContext(ServiceDetailsContext);

    let navigate = useNavigate();

    let checkForAllValidFields = () : boolean => {
        if(signIn){
            if(emailSignInRef.current) if(!emailSignInRef.current.checkValidity()) return false;
            if(passwordSignInRef.current) if(!passwordSignInRef.current.checkValidity()) return false;
        } else {
            if(emailSignUpRef.current) if(!emailSignUpRef.current.checkValidity()) return false;
            if(passwordSignUpRef.current) if(!passwordSignUpRef.current.checkValidity()) return false;
            if(nameSignUpRef.current) if(!nameSignUpRef.current.checkValidity()) return false;
            if(fileSignUpRef.current) if(!fileSignUpRef.current.checkValidity()) return false;
        }
        return true;
    }

    let userInputData = ():any=>{
        let body:InputType = {
            age:"",
            details:"",
            email:"",
            file:null,
            gender:"",
            name:"",
            password:"",
            phone:"",
            pic:""
        };
        if(signIn){
            if(emailSignInRef.current) body.email = emailSignInRef.current.value;
            if(passwordSignInRef.current) body.password = passwordSignInRef.current.value;
        }else{
            if(emailSignUpRef.current) body.email = emailSignUpRef.current.value;
            if(passwordSignUpRef.current) body.password = passwordSignUpRef.current.value;
            if(nameSignUpRef.current) body.name = nameSignUpRef.current.value;
            if(fileSignUpRef.current && fileSignUpRef.current.files) body.file = fileSignUpRef.current?.files[0];
        }
        return body;
    }

    let runCommandChain=(commands:string[],i:number,currentData:any,prevData:any)=>{
        if(i == commands.length) {
            if(prevData.success){
                if(signIn) {
                    alert("Signin Success!");
                }
                else setSignIn(true);
            } else{
                console.log(prevData);
                notificationContext.showNotification("Actually failed to "+(signIn?"signin 😐.":"signup 😶."))
            }
            return;
        }
        (FunctionalityChain.handle(commands[i])(currentData,prevData) as Promise<any>).then(data=>{
            if(data.success) runCommandChain(commands,i+1,currentData,data);
            else notificationContext.showNotification("Actually failed to "+(signIn?"signin 😐.":"signup 😶."))
        }).catch(err=>{
            console.log(err);
            notificationContext.showNotification("Actually failed to "+(signIn?"signin 😐.":"signup 😶."))
        });
    }

    let runCommands = (commands:string[])=>{
        let prevData:any = null;
        let currentData = userInputData();
        runCommandChain(commands,0,currentData,prevData);
    }

    return (
        <div className='UserService'>
            <img src={Bg} alt="" />
            <div className='info'>
                <div className='row tab'>
                    <div className={signIn?'active':""} onClick={()=>setSignIn(true)}>
                        SignIn
                    </div>
                    <div className={!signIn?'active':""} onClick={()=>setSignIn(false)}>
                        SignUp
                    </div>
                </div>
                <div className='window'>
                    {signIn && <div className='flow-card'>
                        <h2>Hi! Welcome to Gina.</h2>
                        <div className='input'>
                            <input placeholder='' type="email" ref={emailSignInRef} id='email' />
                            <label htmlFor="email">Email</label>
                        </div>
                        <div className='input'>
                            <input placeholder='' type="password" ref={passwordSignInRef} id='pass' />
                            <label htmlFor="pass">Password</label>
                        </div>
                        <button onClick={()=>runCommands([
                            FUNCTIONALILTY_COMMANDS.SERVICE_SIGN_IN
                        ])}>Let's go</button>
                    </div>}
                    {!signIn && <div className='flow-card'>
                        <h2>Create an account.</h2>
                        <div className='input'>
                            <input placeholder='' type="text" id='name' ref={nameSignUpRef} />
                            <label htmlFor="name">Name</label>
                        </div>
                        <div className='input'>
                            <input placeholder='' type="email" id='email' ref={emailSignUpRef} />
                            <label htmlFor="email">Email</label>
                        </div>
                        <div className='input'>
                            <input placeholder='' type="password" id='pass' ref={passwordSignUpRef} />
                            <label htmlFor="pass">Password</label>
                        </div>
                        <div className='input'>
                            <input placeholder='' type="file" id='file' ref={fileSignUpRef} />
                            <label htmlFor="file">Profile Pic</label>
                        </div>
                        <button onClick={()=>{
                            runCommands([
                                FUNCTIONALILTY_COMMANDS.UPLOAD,
                                FUNCTIONALILTY_COMMANDS.SERVICE_SIGN_UP
                            ]);
                        }}>Let's go</button>
                    </div>}
                </div>
            </div>
        </div>
    );
};

export default Service;