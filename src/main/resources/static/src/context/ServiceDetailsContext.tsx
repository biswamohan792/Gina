import { useState, createContext } from "react";

export type ServiceDetails = {
    serviceId : String;
    name : String;
    pic : String;
    getPermission?: boolean;
    url?: String;
}

export let context = createContext<{
    serviceDetailsState : ServiceDetails,
    setServiceDetailsState : React.Dispatch<React.SetStateAction<ServiceDetails>>
}>({
    serviceDetailsState : {} as ServiceDetails,
    setServiceDetailsState : {} as any
});

export default ({children}:{
    children:any
}) => {

    let [serviceDetailsState,setServiceDetailsState] = useState<ServiceDetails>({
        name:"",
        pic:"",
        serviceId:""
    });

    return (
        <context.Provider value={{serviceDetailsState,setServiceDetailsState}}>
            {children}
        </context.Provider>
    );
}