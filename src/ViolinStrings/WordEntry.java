package ViolinStrings;

/*
 * @(#)Strings.java 1.00 2000/05/31
 *
 *
 *  Copyright notice:
 *
 * (C) Michael Schmeling 1998, 2000 - All Rights Reserved
 *
 * This software is provided 'as-is', without any express or implied
 * warranty.  In no event will the author be held liable for any damages
 * arising from the use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 *
 * 1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 3. Altered version of the source code or the class files must be stored
 *    with a different Java package name, so that users can clearly distinguish
 *    those altered versions from the original software and can use them safely
 *    together with the original software.
 * 4. This notice may not be removed or altered from any source distribution.
 *
 * Michael Schmeling
 * MSchmelng@aol.com
 *
 * If you use the library in a commercial product I would appreciate that
 * you send me a few lines with a short description of your product.
 * However, this is not a requirement.
 * The source is provided for free but without warranty of any kind.
 * The library has been entirely written by Michael Schmeling, it does not
 * contain third-party code.
 *
 * THE AUTHOR MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
 * SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. THE AUTHOR SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 *
 *
 * Contributions to this package where made by:
 *    Eric Jablow
 *
 */

import java.util.*;

class WordEntry
{
   public int start;
   public int len;
   public int wordNum;

   public WordEntry(int start, int len, int wordNum)
   {
	  this.start   = start;
	  this.len     = len;
	  this.wordNum = wordNum;
   }   
}
