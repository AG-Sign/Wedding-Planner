import { Switch, Route, Redirect } from "react-router-dom";
import { useContext, useEffect } from "react";

import Layout from "./components/Layout/Layout";
import UserProfile from "./components/profile/userProfile";
import AuthContext from "./components/context/auth-context";
import Login from "./components/Auth/Login";
import Signup from "./components/Auth/Signup";
import ProviderProfile from "./components/profile/providerProfile";
import Reservations from "./components/reservations/reservations";

function App() {
  const ctx = useContext(AuthContext);
  useEffect(() => {
    document.title = "Wedding Planner";
  }, []);
  let image 
  if(ctx.isLoggedIn){
    image=`url("https://img.freepik.com/free-photo/pink-defocused-glittery-background-design_53876-104793.jpg?size=626&ext=jpg")`
  }else{
    image = `url("https://i.pinimg.com/564x/d6/24/6c/d6246cff4edf0d6e289e90f999b65afa.jpg")`
  }
  return (
    <div
      style={{
        backgroundImage: `${image}`,backgroundPosition: 'center',
        backgroundSize: 'cover',
        backgroundRepeat: 'no-repeat',
        width: '100vw',
        height: '100vh'
      }}
    >
      <Layout>
        <Switch>
          {!ctx.isLoggedIn && (
            <Route path="/auth">
              <Login />
            </Route>
          )}
          {!ctx.isLoggedIn && (
            <Route path="/signup">
              <Signup />
            </Route>
          )}
          {ctx.isLoggedIn && (
            <Route path="/profile">
              <UserProfile />
            </Route>
          )}
          {ctx.isLoggedIn && (
            <Route path="/providerProfile">
              <ProviderProfile />
            </Route>
          )}
          {ctx.isLoggedIn && (
            <Route path="/reservations">
              <Reservations />
            </Route>
          )}
          {ctx.isLoggedIn && ctx.type === "Provider" && <ProviderProfile />}
          {ctx.isLoggedIn && ctx.type === "Customer" && <UserProfile />}
          <Route path="*">
            <Redirect to="/auth" />
          </Route>
        </Switch>
      </Layout>
    </div>
  );
}

export default App;