import {Route, Switch} from "react-router";
import {CreateWorldPage} from "./create";
import React from "react";
import {IsAuthenticated} from "../common/Authenticated";

export const WorldsRouter = () => {
    return (
        <IsAuthenticated render={auth => (
            <Switch>
                {auth && <Route path="/worlds/create" exact={true} render={() => <CreateWorldPage/>} />}
            </Switch>
        )} />
    )
};