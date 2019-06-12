import {configure} from 'enzyme';
import Adapter from 'enzyme-adapter-react-16';
import 'jest-enzyme';
import './i18n';

configure({ adapter: new Adapter() });
