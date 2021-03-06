import i18n from 'i18next';
import {initReactI18next} from "react-i18next";
import LanguageDetector from 'i18next-browser-languagedetector';
import defaultTranslations from './messages.json';

i18n
    .use(LanguageDetector)
    .use(initReactI18next)
    .init({
        resources: {
            dev: {
                goworlds: defaultTranslations
            }
        },

        // have a common namespace used around the full app
        ns: ['goworlds'],
        defaultNS: 'goworlds',

        nsSeparator: false,

        debug: false,

        interpolation: {
            escapeValue: false
        },

        parseMissingKeyHandler: (key) => `!!${key}!!`
    });

export default i18n;
