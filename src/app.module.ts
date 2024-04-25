import { Module } from '@nestjs/common';
import {APP_FILTER} from "@nestjs/core";
import {GlobalExceptionFilter} from "./common/error/global-exception-filter";

@Module({
  imports: [],
  controllers: [],
  providers: [
    {
      provide: APP_FILTER,
      useClass: GlobalExceptionFilter
    }
  ],
})
export class AppModule {}
