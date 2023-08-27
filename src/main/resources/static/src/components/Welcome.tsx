import React, { useContext, useEffect, useState } from "react";
import "./Welcome.scss";
import Pic from "../pics/bg.jpg";
import { useLocation, useNavigate } from "react-router-dom";
import FunctionalityChain, {
  FUNCTIONALILTY_COMMANDS,
  UserResponse,
} from "../services/FunctionalityChain";
import { context as ServiceDetailsContext } from "../context/ServiceDetailsContext";
import { context as NotificationContext } from "../context/NotificationContext";

const Welcome = () => {
  const location = useLocation();
  const serviceDetailsContext = useContext(ServiceDetailsContext);
  const notificationContext = useContext(NotificationContext);
  const navigate = useNavigate();

  const [state, setState] = useState({
    loading: false,
    permissionBox: false,
    defaultView: false,
  });

  const openServiceUrlOrDefaultDisplay = () => {
    if (serviceDetailsContext.serviceDetailsState?.url) {
      setState({ ...state, loading: true });
    } else setState({ ...state, defaultView: true });
  };

  useEffect(() => {
    if (location.state && location.state.user) {
      if (serviceDetailsContext.serviceDetailsState?.getPermission) {
        setState({ ...state, permissionBox: true });
      } else openServiceUrlOrDefaultDisplay();
    } else {
      navigate("/user");
    }
  }, []);

  useEffect(() => {
    if (state.loading) {
      setTimeout(() => {
        if (serviceDetailsContext.serviceDetailsState?.url) {
          let userId = (location.state?.user as UserResponse)?.userId;
          window.open(
            (serviceDetailsContext.serviceDetailsState.url as string) +
              "?userId=" +
              userId,
            "_self"
          );
        } else
          setState({
            defaultView: true,
            loading: false,
            permissionBox: false,
          });
      }, 5000);
    }
  }, [state.loading]);

  return (
    <div className="Welcome">
      {serviceDetailsContext.serviceDetailsState?.pic && (
        <img
          src={
            "http://localhost:3005/file/pic?name=" +
            serviceDetailsContext.serviceDetailsState?.pic
          }
          alt=""
          loading="lazy"
          draggable={false}
        />
      )}
      <img
        src={
          "http://localhost:3005/file/pic?name=" +
          (location.state?.user as UserResponse)?.pic
        }
        alt=""
        loading="lazy"
        draggable={false}
      />
      <div className="details">
        <h2>Gina Auth System</h2>
        {state.defaultView && (
          <p className="defaultline">Your successfully logged in. 😜</p>
        )}
        {state.loading && (
          <div className="inner">
            <div className="sninner">
              <div className="lds-roller">
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
              </div>
            </div>
            <p>Logging you in...</p>
          </div>
        )}
        {state.permissionBox && (
          <div className="permission">
            Hi {(location.state?.user as UserResponse)?.name}, Will you give
            this service access to your info?
            <div className="control">
              <button
                className="good"
                onClick={() => {
                  (
                    FunctionalityChain.handle(
                      FUNCTIONALILTY_COMMANDS.GIVE_SERVICE_ACCESS_TO_USER
                    )(null, {
                      userId: (location.state?.user as UserResponse)?.userId,
                      serviceId:
                        serviceDetailsContext.serviceDetailsState.serviceId,
                    }) as Promise<any>
                  )
                    .then((res) => {
                      if (res.success)
                        setState({
                          defaultView: false,
                          loading: true,
                          permissionBox: false,
                        });
                      else
                        notificationContext.showNotification(
                          "Permission Access Failed!"
                        );
                    })
                    .catch((err) => {
                      notificationContext.showNotification(
                        "Permission Access Failed!"
                      );
                    });
                }}
              >
                Confirm
              </button>
              <button
                onClick={() => {
                  setState({
                    defaultView: true,
                    loading: false,
                    permissionBox: false,
                  });
                }}
              >
                Cancel
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Welcome;
