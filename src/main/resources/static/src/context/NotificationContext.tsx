import { useState, createContext } from "react";
import Notification from "../components/Notification";

export let context = createContext<{showNotification:Function}>({showNotification:(error:string)=>{}});

export default ({children}:{
    children:any
}) => {

    let [notiState,setNotiState] = useState({
        show:false,
        error:""
    });

    let showNotification=(error:string)=>{
        setNotiState({
            show:true,
            error:error
        });
    }

    return (
        <context.Provider value={{showNotification}}>
            {children}
            {notiState.show && <Notification notiState={notiState} setNotiState={setNotiState}/>}
        </context.Provider>
    );
}