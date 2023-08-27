import React, { useState, createRef, useContext, useEffect } from 'react';
import './AuthUserService.scss';
import Bg from '../pics/bg.jpg';
import {context as NotificationContext} from '../context/NotificationContext';
import FunctionalityChain, { FUNCTIONALILTY_COMMANDS, UserResponse } from '../services/FunctionalityChain';
import { InputType } from '../services/AuthService';
import {context as ServiceDetailsContext, ServiceDetails} from '../context/ServiceDetailsContext';
import { useNavigate } from 'react-router-dom';

const User = () => {

    let [signIn,setSignIn] = useState(true);

    let emailSignInRef = createRef<HTMLInputElement>();
    let passwordSignInRef = createRef<HTMLInputElement>();

    let emailSignUpRef = createRef<HTMLInputElement>();
    let passwordSignUpRef = createRef<HTMLInputElement>();
    let nameSignUpRef = createRef<HTMLInputElement>();
    let ageSignUpRef = createRef<HTMLInputElement>();
    let phoneSignUpRef = createRef<HTMLInputElement>();
    let fileSignUpRef = createRef<HTMLInputElement>();
    let genderSignUpRef = createRef<HTMLSelectElement>();

    let notificationContext = useContext(NotificationContext);
    let serviceDetailsContext = useContext(ServiceDetailsContext);

    let navigate = useNavigate();

    useEffect(()=>{
        try{
            const queryParams = new URLSearchParams(window.location.search);
            const text = queryParams.get('service');
            if(text){
                const serviceDetails:ServiceDetails = JSON.parse(text) as ServiceDetails;
                serviceDetailsContext.setServiceDetailsState(serviceDetails);
                window.history.replaceState(null,'',window.location.pathname);
            }
        }catch(e:any){}
    },[]);

    let checkForAllValidFields = () : boolean => {
        if(signIn){
            if(emailSignInRef.current) if(!emailSignInRef.current.checkValidity()) return false;
            if(passwordSignInRef.current) if(!passwordSignInRef.current.checkValidity()) return false;
        } else {
            if(emailSignUpRef.current) if(!emailSignUpRef.current.checkValidity()) return false;
            if(passwordSignUpRef.current) if(!passwordSignUpRef.current.checkValidity()) return false;
            if(nameSignUpRef.current) if(!nameSignUpRef.current.checkValidity()) return false;
            if(ageSignUpRef.current) if(!ageSignUpRef.current.checkValidity()) return false;
            if(phoneSignUpRef.current) if(!phoneSignUpRef.current.checkValidity()) return false;
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
            if(ageSignUpRef.current) body.age = ageSignUpRef.current.value;
            if(phoneSignUpRef.current) body.phone = phoneSignUpRef.current.value;
            if(fileSignUpRef.current && fileSignUpRef.current.files) body.file = fileSignUpRef.current?.files[0];
            if(genderSignUpRef.current) body.gender = genderSignUpRef.current.value;
        }
        return body;
    }

    let askPermissionFromUser = (user:UserResponse)=>{
        navigate("/welcome",{state:{user}})
    } 

    let runCommandChain=(commands:string[],i:number,currentData:any,prevData:any)=>{
        if(i == commands.length) {
            if(prevData.success){
                if(signIn) askPermissionFromUser(prevData.body);
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
            <img src={Bg} alt="" loading='lazy' />
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
                            <input required={true} ref={emailSignInRef} placeholder='' type="email" id='email' />
                            <label htmlFor="email">Email</label>
                        </div>
                        <div className='input'>
                            <input required={true} ref={passwordSignInRef} placeholder='' type="password" id='pass' />
                            <label htmlFor="pass">Password</label>
                        </div>
                        <button onClick={()=>{
                            if(checkForAllValidFields()){
                                runCommands([
                                    FUNCTIONALILTY_COMMANDS.USER_LOG_IN,
                                ]);
                            } else {
                                notificationContext.showNotification("Hi! Forgot to fill in values 🫡.")
                            }
                        }}>Let's go</button>
                    </div>}
                    {!signIn && <div className='flow-card'>
                        <h2>Create an account.</h2>
                        <div className='input'>
                            <input required={true} ref={nameSignUpRef} placeholder='' type="text" id='name' />
                            <label htmlFor="name">Name</label>
                        </div>
                        <div className='input'>
                            <input required={true} ref={emailSignUpRef} placeholder='' type="email" id='email' />
                            <label htmlFor="email">Email</label>
                        </div>
                        <div className='input'>
                            <input required={true} ref={passwordSignUpRef} placeholder='' type="password" id='pass' />
                            <label htmlFor="pass">Password</label>
                        </div>
                        <div className='input'>
                            <input required={true} ref={ageSignUpRef} placeholder='' type="number" id='age' />
                            <label htmlFor="age">Age</label>
                        </div>
                        <div className='input'>
                            <input required={true} ref={phoneSignUpRef} placeholder='' type="number" id='phone' />
                            <label htmlFor="phone">Phone</label>
                        </div>
                        <div className='input'>
                            <input required={true} ref={fileSignUpRef} placeholder='' type="file" id='file' />
                            <label htmlFor="file">Profile Pic</label>
                        </div>
                        <div className='input'>
                            <select ref={genderSignUpRef}>
                                <option value={"female"}>FEMALE</option>
                                <option value={"male"}>MALE</option>
                            </select>
                        </div>
                        <button onClick={()=>{
                            if(checkForAllValidFields()){
                                runCommands([
                                    FUNCTIONALILTY_COMMANDS.UPLOAD,
                                    FUNCTIONALILTY_COMMANDS.USER_SIGN_UP
                                ]);
                            } else {
                                notificationContext.showNotification("Hi! Forgot to fill in values 🫡.")
                            }
                        }}>Let's go</button>
                    </div>}
                </div>
            </div>
        </div>
    );
};

export default User;