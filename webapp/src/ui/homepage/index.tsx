import React, {FunctionComponent} from 'react';
import {Grid, Image, Segment, Header} from "semantic-ui-react";
import {useTranslation} from "react-i18next";

import homepageImage from './homepage.jpg';
import {Login} from "./Login";

/** The props that an HomePage needs */
type HomePageProps = {};

/**
 * The home page
 * @constructor
 */
export const HomePage: FunctionComponent<HomePageProps> = () => {
    const { t } = useTranslation();

    return (
        <Grid>
            <Grid.Column width={12}>
                <Segment raised compact>
                    <Header>
                        {t('page.header')}
                    </Header>
                    <Image src={homepageImage} rounded />
                </Segment>
            </Grid.Column>
            <Grid.Column width={4}>
                <Login/>
            </Grid.Column>
        </Grid>
    );
};
