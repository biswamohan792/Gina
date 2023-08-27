import React from 'react';
import './Notification.scss';

const Notification = ({
    notiState,setNotiState
}:{
    notiState:{
        show:boolean,
        error:string
    },
    setNotiState:React.Dispatch<React.SetStateAction<{
        show: boolean;
        error: string;
    }>>
}) => {
    return (
        <div className='notification'>
            <p>Issue</p>
            <h3>{notiState.error}</h3>
            <button onClick={()=>{
                setNotiState({
                    show:false,
                    error:""
                });
            }}>Ok!</button>
        </div>
    );
};

export default Notification;