import { ErrorProperty } from './error-property';

export class BusinessException extends Error {
  readonly errorProperty: ErrorProperty;
}
