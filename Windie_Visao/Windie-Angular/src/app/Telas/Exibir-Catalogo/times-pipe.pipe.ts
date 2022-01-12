/*import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'timesPipe'
})
export class TimesPipePipe implements PipeTransform {

  transform(value: unknown, ...args: unknown[]): unknown {
    return null;
  }

}*/


import {PipeTransform, Pipe} from '@angular/core';

@Pipe({name: 'times'})
export class TimesPipePipe implements PipeTransform {
  transform(value: number): any {
    const iterable = <Iterable<any>> {};
    iterable[Symbol.iterator] = function* () {
      let n = 0;
      while (n < value) {
        yield ++n;
      }
    };
    return iterable;
  }
}
